package com.corgi.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.corgi.activity.api.CorgiActivityFeedService;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.common.CorgiQueueName;
import com.corgi.common.messages.PushMessage;
import com.corgi.mapper.CorgiInviteUserMapper;
import com.corgi.mapper.CorgiOrderMapper;
import com.corgi.mapper.CorgiReservationMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.api.CorgiPicService;

import com.corgi.user.api.CorgiUserWechatService;
import com.corgi.user.entity.*;
import com.corgi.user.enums.MerchandiseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiOrderService.class)
@Slf4j
@Component
public class CorgiOrderServiceImpl implements CorgiOrderService {

    @Reference
    private CorgiActivityFeedService corgiActivityFeedService;

    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private CorgiBillboardService corgiBillboardService;
    @Autowired
    private CorgiUserWechatService corgiUserWechatService;
    @Autowired
    private CorgiReservationMapper corgiReservationMapper;
    @Autowired
    private CorgiOrderMapper corgiOrderMapper;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private CorgiInviteUserMapper corgiInviteUserMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public List<CorgiMerchandise> getMerchandise(CorgiMerchandise merchandise) {
        return corgiOrderMapper.searchMerchandise(merchandise);
    }

    @Override
    public void addOrder(CorgiOrder order) {
        corgiOrderMapper.addOrder(order);
    }

    @Override
    public void updateOrder(CorgiOrder order) {
        try {
            corgiOrderMapper.addLog(order);
            if (!CorgiOrder.PAY_TYPE.WITHDRAW.equals(order.getPayType())) {
                order.setResult(null);
            }
            String expiresDate = null;
            if (CorgiOrder.PAY_TYPE.IN_APP.equals(order.getPayType())) {
                expiresDate = order.getBuyerId();
                order.setBuyerId(null);
            }
            if (StringUtils.isNotEmpty(order.getOrderId()) && CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                List<CorgiOrder> oldOrders = corgiOrderMapper.getOrderByOrderId(order.getOrderId());
                if (CollectionUtils.isNotEmpty(oldOrders)) {
                    for (CorgiOrder oldOrder : oldOrders) {
                        if (CorgiOrder.STATUS.SUCCESS.equals(oldOrder.getStatus())
                                && !oldOrder.getTradeNo().equals(order.getTradeNo())) {
                            order.setStatus("duplicated");
                        }
                    }
                }
            }
            corgiOrderMapper.updateOrder(order);
            if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                String preExpireTime = corgiUserMapper.getVipExpire(order.getUserId());

                this.buy(order.getTradeNo(), order.getMerchId(), expiresDate);

                String afterExpireTime = corgiUserMapper.getVipExpire(order.getUserId());
                //购买vip更新昵称更改时间
                if (!StringUtils.isEmpty(afterExpireTime) && !"-".equals(afterExpireTime)
                        && (StringUtils.isEmpty(preExpireTime) || "-".equals(preExpireTime))) {
                    redisTemplate.delete("update_nickname_" + order.getUserId());
                }
            }
        } catch (Exception e) {
            order.setResult(e.getMessage());
            corgiOrderMapper.addLog(order);
        }
    }

    @Override
    public void addLog(String result, String transactionId, String originId) {
        corgiOrderMapper.addAppstoreLog(result, transactionId, originId);
    }

    @Override
    public List<CorgiOrder> getOrderByPage(CorgiOrder order, Integer page, Integer pageSize) {
        return corgiOrderMapper.getOrderByPage(order, (page - 1) * pageSize, pageSize);
    }

    @Override
    public Integer countOrder(CorgiOrder order) {
        return corgiOrderMapper.countOrder(order);
    }

    @Override
    public CorgiOrder getOrderByTradeNo(String tradeNo) {
        return corgiOrderMapper.getOrderByTradeNo(tradeNo);
    }

    @Override
    public CorgiMerchandise getMerchandiseById(String merchId, String userId) {
        CorgiMerchandise merchandise = corgiOrderMapper.getMerchandiseById(merchId);
        if (merchandise == null) {
            List<CorgiMerchandise> merchandises = corgiOrderMapper.getMerchandiseByAppMerchId(merchId);
            if (CollectionUtils.isEmpty(merchandises)) {
                return null;
            }
            if (merchandises.size() == 1) {
                return merchandises.get(0);
            }
            if (CorgiMerchandise.SUBSCRIBE.equals(merchandises.get(0).getType())) {
                CorgiUserGoods orderQuery = CorgiUserGoods.builder()
                        .userId(userId)
                        .goodsType(CorgiUserGoods.GOODS_TYPE.SUBSCRIBE)
                        .start(0)
                        .size(1)
                        .build();
                List<CorgiUserGoods> orders = this.getUserGoods(orderQuery);
                if (CollectionUtils.isNotEmpty(orders)) {
                    merchandises = merchandises.stream().filter(m -> CorgiMerchandise.SUBSCRIBE.equals(m.getType()) && !MerchandiseEnum.isFirst(m.getId())).collect(Collectors.toList());
                } else {
                    merchandises = merchandises.stream().filter(m -> CorgiMerchandise.SUBSCRIBE.equals(m.getType()) && MerchandiseEnum.isFirst(m.getId())).collect(Collectors.toList());
                }
            }
            if (CollectionUtils.isEmpty(merchandises)) {
                return null;
            }
            return merchandises.get(0);
        }
        return merchandise;
    }

    @Override
    public String buy(String tradeNo, String merchId, String expiresDate) {
        String key = "buying_goods_" + tradeNo;
        try {
            this.lock(key);
            CorgiOrder order = corgiOrderMapper.getOrderByTradeNo(tradeNo);
            if (corgiOrderMapper.countGoodsByTradeNo(tradeNo) > 0) {
                return null;
            }
            order.setMerchId(merchId);
            CorgiUserGoods goods = CorgiUserGoods.builder()
                    .userId(order.getUserId())
                    .currency(CorgiUserGoods.CURRENCY.CNY)
                    .price(order.getPayAmount())
                    .tradeNo(order.getTradeNo())
                    .merchId(order.getMerchId())
                    .build();
            CorgiMerchandise merchandise = corgiOrderMapper.getMerchandiseById(order.getMerchId());
            order.setResult(merchandise.toString() + "=" + order.getMerchId());
            corgiOrderMapper.addLog(order);
            if (merchandise.getType().startsWith(CorgiMerchandise.SUBSCRIBE)) {
                if (!this.buySubscribe(goods, merchandise, order, expiresDate)) {
                    return null;
                }
            } else if (CorgiMerchandise.ACTIVITY.equals(merchandise.getType())) {
                this.buyActivity(goods, order);
            } else if (CorgiMerchandise.BILLBOARD.equals(merchandise.getType())) {
                this.buyBillboard(goods, order, merchandise);
            } else if (CorgiMerchandise.RESERVE.equals(merchandise.getType())) {
                this.buyReserve(goods, order);
            } else if (CorgiMerchandise.LOCATION.equals(merchandise.getType())) {
                this.buyLocation(goods, order);
            } else if (CorgiMerchandise.LOCATION_MONTH.equals(merchandise.getType())) {
                this.buyLocationMonth(goods, order);
            } else if (CorgiMerchandise.WECHAT.equals(merchandise.getType())) {
                this.buyWechat(goods, order);
            } else {
                this.buyGoods(goods, merchandise);
            }
        } finally {
            this.unlock(key);
        }
        return null;
    }

    private boolean buyWechat(CorgiUserGoods goods, CorgiOrder order) {
        UserWechat wechat = corgiUserWechatService.getUserWechat(order.getSellerId());
        if (wechat != null) {
            goods.setGoodsType(CorgiMerchandise.WECHAT);
            goods.setGoodsId(order.getSellerId());
            goods.setMarketId(wechat.getId());
            goods.setTraderId(order.getSellerId());
            goods.setDesc("购买成功");
            corgiOrderMapper.addGoods(goods);
            rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildWechatMessage(goods));
            rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildWechatReplyMessage(goods, wechat.getReply()));
            return true;
        } else {
            order.setResult("该用户微信不存在");
            corgiOrderMapper.addLog(order);
            return false;
        }
    }

    private boolean buyLocationMonth(CorgiUserGoods goods, CorgiOrder order) {
        goods.setGoodsType(CorgiMerchandise.LOCATION_MONTH);
        goods.setGoodsId("-");
        goods.setTraderId("corgi");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String locationExpireDate = corgiOrderMapper.getLocationExpireDate(order.getUserId());
        Date expireDate;
        try {
            if (StringUtils.isEmpty(locationExpireDate) || (expireDate = sdf.parse(locationExpireDate)).compareTo(new Date()) <= 0) {
                expireDate = new Date();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            expireDate = new Date();
            order.setResult(e.getMessage());
            corgiOrderMapper.addLog(order);
        }
        String finalDate = "";
        calendar.setTime(expireDate);
        calendar.add(Calendar.DATE, 30);
        finalDate = sdf.format(calendar.getTime());
        goods.setDesc("购买成功，日期截止至 " + finalDate);
        goods.setMarketId(finalDate);
        corgiOrderMapper.addGoods(goods);
        rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildLocationMonthMessage(goods, finalDate.substring(0, 10)));
        return true;
    }

    private void buyLocation(CorgiUserGoods goods, CorgiOrder order) {
        UserPosition position = corgiUserMapper.getUserPosition(order.getMarketId());
        goods.setGoodsType(CorgiMerchandise.LOCATION);
        goods.setGoodsId(order.getMarketId());
        goods.setTraderId("corgi");
        goods.setMarketId(order.getMarketId());
        goods.setDesc("购买成功");
        corgiOrderMapper.addGoods(goods);
        if (position == null) {
            position = new UserPosition();
        }
        order.setResult("付费定位：".concat(JSONObject.toJSONString(position)));
        corgiOrderMapper.addLog(order);
    }

    private void buyReserve(CorgiUserGoods goods, CorgiOrder order) {
        BarReservation reservation = corgiReservationMapper.getReservationById(order.getMarketId());
        if (reservation != null) {
            goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.RESERVE);
            goods.setGoodsId(reservation.getId());
            goods.setDesc("购买成功");
            goods.setMarketId(reservation.getId());
            goods.setTraderId(reservation.getBarId());
            corgiOrderMapper.addGoods(goods);
            reservation.setTradeNo(order.getTradeNo());
            reservation.setStatus("paid");
            corgiReservationMapper.updateBarReservation(reservation);
        } else {
            order.setResult("reservation can not be found");
            corgiOrderMapper.addLog(order);
        }
    }

    private void buyBillboard(CorgiUserGoods goods, CorgiOrder order, CorgiMerchandise merchandise) {
        goods.setGoodsType(merchandise.getType());
        goods.setGoodsId(order.getMarketId());
        goods.setTraderId("corgi");
        goods.setMarketId(order.getMarketId());
        goods.setDesc("购买成功");
        corgiOrderMapper.addGoods(goods);
        PaidBillboard query = new PaidBillboard();
        query.setId(order.getMarketId());
        query.setTradeNo(order.getTradeNo());
        query.setStatus(PaidBillboard.PAID);
        corgiBillboardService.updatePaiBillboard(query);
        rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildBillboardMessage(goods));
    }

    private void buyGoods(CorgiUserGoods goods, CorgiMerchandise merchandise) {
        goods.setGoodsType(merchandise.getType());
        goods.setGoodsId(merchandise.getId());
        goods.setTraderId("corgi");
        goods.setMarketId("-");
        goods.setDesc("购买成功");
        corgiOrderMapper.addGoods(goods);
    }

    private void buyActivity(CorgiUserGoods goods, CorgiOrder order) {
        CorgiUserMarket market = corgiOrderMapper.getMarketById(order.getMarketId());
        if (market != null) {
            goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.ACTIVITY);
            goods.setGoodsId(market.getSourceId());
            goods.setMarketId(market.getId());
            goods.setTraderId(market.getUserId());
            goods.setDesc("购买成功");
            corgiOrderMapper.addGoods(goods);
            if (market.getMerchId().equals(order.getMerchId())) {
                rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildActivityMessage(goods));
            } else {
                goods.setDesc("购买价格不是原本定价");
                goods.setStatus("0");
                corgiOrderMapper.updateUserGoods(goods);
            }
        } else {
            order.setResult("user market can not be found");
            corgiOrderMapper.addLog(order);
        }
    }

    private boolean buySubscribe(CorgiUserGoods goods, CorgiMerchandise merchandise, CorgiOrder order, String expiresDate) {
        goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.SUBSCRIBE);
        goods.setGoodsId(merchandise.getId());
        goods.setTraderId("corgi");
        goods.setMarketId("-");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vipExpireDate = corgiUserMapper.getVipExpire(order.getUserId());
        Date expireDate;
        try {
            if ("-".equals(vipExpireDate) || (expireDate = sdf.parse(vipExpireDate)).compareTo(new Date()) <= 0) {
                expireDate = new Date();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            expireDate = new Date();
            order.setResult(e.getMessage());
            corgiOrderMapper.addLog(order);
        }
        MerchandiseEnum e = MerchandiseEnum.getByCode(merchandise.getId().replaceAll("SA", "S"));
        if (e != null) {
            String finalDate = "";
            if (StringUtils.isNotEmpty(expiresDate)) {
                calendar.setTime(new Date(Long.valueOf(expiresDate)));
            } else {
                calendar.setTime(expireDate);
                calendar.add(Calendar.DATE, e.getDays());
            }
            finalDate = sdf.format(calendar.getTime());
            corgiUserMapper.updateVipExpire(order.getUserId(), "1", finalDate);
            goods.setDesc("购买成功，日期截止至 " + finalDate);
            corgiOrderMapper.addGoods(goods);
            rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildSubscribeMessage(goods, e.getDays(), finalDate.substring(0, 10)));
        } else {
            order.setResult("merchandise can not be found");
            corgiOrderMapper.addLog(order);
        }
        return true;
    }

    @Override
    public void subscribe(CorgiOrder order, CorgiUserGoods goods, String vipStatus, String finalDate) {
        if ("1".equals(vipStatus)) {
            if (!StringUtils.isEmpty(order.getTradeNo())) {
                corgiOrderMapper.addOrder(order);
                corgiOrderMapper.updateOrder(order);
                corgiOrderMapper.addLog(order);
            } else if (StringUtils.isNotEmpty(order.getUserId())) {
                order.setTradeNo(order.getUserId());
                order.setResult(finalDate);
                corgiOrderMapper.addLog(order);
            } else if (StringUtils.isNotEmpty(order.getOrderId())) {
                List<CorgiOrder> orders = corgiOrderMapper.getOrderByOrderId(order.getOrderId());
                for (CorgiOrder order1 : orders) {
                    if (CorgiOrder.STATUS.SUCCESS.equals(order1.getStatus())) {
                        order.setUserId(order1.getUserId());
                        order.setStatus(CorgiOrder.STATUS.SUCCESS);
                        break;
                    }
                }
            }
            if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                if (goods != null) {
                    corgiOrderMapper.addGoods(goods);
                }
                if (StringUtils.isNotEmpty(order.getUserId())) {
                    String nowDate = corgiUserMapper.getVipExpire(order.getUserId());
                    if (!"-".equals(nowDate) && StringUtils.isNotEmpty(nowDate)
                            && nowDate.compareTo(finalDate) > 0) {
                        List<CorgiUserGoods> oldGoods = corgiOrderMapper.getUserGoodsByMerchIds(order.getUserId(),
                                String.join("','", Arrays.asList("SA01", "SA02", "SA03", "SA04", "SA05", "SA06", "SA06", "SA08", "BS01")));
                        if (!CollectionUtils.isEmpty(oldGoods)) {
                            return;
                        }
                    }
                    corgiUserMapper.updateVipExpire(order.getUserId(), "1", finalDate);
                }
            }
        } else if ("0".equals(vipStatus)) {
            List<CorgiOrder> orders = corgiOrderMapper.getOrderByOrderId(order.getOrderId());
            for (CorgiOrder order1 : orders) {
                if (CorgiOrder.STATUS.SUCCESS.equals(order1.getStatus())) {
                    order.setTradeNo(order1.getTradeNo());
                    corgiOrderMapper.addLog(order);
                    corgiUserMapper.updateVipExpire(order1.getUserId(), "0", null);
                }
            }
        }
    }

    @Override
    public Integer countInvited(String userId, String type) {
        return corgiInviteUserMapper.countInvite(userId);
    }

    @Override
    public Boolean invite(String userId, String inviteId) {
        UserLogin userLogin = corgiUserMapper.getUserLogin(inviteId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        boolean isOldUser = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())
                .compareTo(userLogin.getCtime()) > 0;
        if (isOldUser) {
            return false;
        }
        if (corgiInviteUserMapper.countInviteTel(userLogin.getTelNo()) > 0) {
            return false;
        }
        corgiInviteUserMapper.addInvite(userId, inviteId, userLogin.getTelNo());
        String messageKey = "invite-" + inviteId + "-inviter-" + userId;
        if (redisTemplate.opsForValue().setIfAbsent(messageKey, inviteId, 1L, TimeUnit.HOURS)) {
            rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildInvitedMessage(userId, inviteId));
        }
        String lockKey = "invite-" + userId;
        try {
            for (int i = 0; i < 10; i++) {
                if (redisTemplate.opsForValue().setIfAbsent(lockKey, inviteId, 1L, TimeUnit.MINUTES)) {
                    MerchandiseEnum e = MerchandiseEnum.BONUS_SUBSCRIBE;
                    List<CorgiUserGoods> gotGoods = corgiOrderMapper.getUserGoods(CorgiUserGoods.builder()
                            .userId(userId)
                            .merchId(e.getCode())
                            .start(0)
                            .size(20)
                            .build());
                    if (gotGoods.size() >= 12) {
                        return false;
                    }
                    Integer inviteCount = corgiInviteUserMapper.countInvite(userId);
                    Integer shouldBonus = inviteCount / 10 > 12 ? 12 : inviteCount / 10;
                    for (int j = 0; j < shouldBonus - gotGoods.size(); j++) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String vipExpireDate = corgiUserMapper.getVipExpire(userId);
                        Date expireDate = new Date();
                        try {
                            if ("-".equals(vipExpireDate) || (expireDate = sdf.parse(vipExpireDate)).compareTo(new Date()) <= 0) {
                                expireDate = new Date();
                            }
                        } catch (Exception ex) {
                            log.error(ex.getMessage(), ex);
                        }
                        String finalDate = "";
                        calendar.setTime(expireDate);
                        calendar.add(Calendar.DATE, e.getDays());
                        finalDate = sdf.format(calendar.getTime());
                        corgiUserMapper.updateVipExpire(userId, "1", finalDate);
                        corgiOrderMapper.addGoods(CorgiUserGoods.builder()
                                .goodsType("bonusSubscribe")
                                .userId(userId)
                                .currency(CorgiUserGoods.CURRENCY.CNY)
                                .merchId(e.getCode())
                                .desc(e.getDesc())
                                .price(0.0)
                                .tradeNo("-")
                                .traderId("corgi")
                                .marketId("-")
                                .goodsId(e.getCode())
                                .build());
                    }
                    rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildBonusMessage(userId));
                    return true;
                }
                Thread.sleep(100l);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete(lockKey);
        }
        return false;
    }

    @Override
    public void updateReceipt(String tradeNo, String receipt) {
        corgiOrderMapper.updateReceipt(tradeNo, receipt);
    }

    @Override
    public String getReceipt(String userId, String receipt) {
        return corgiOrderMapper.getReceipt(userId, receipt);
    }

    @Override
    public String addUserMarket(CorgiUserMarket market) {
        corgiOrderMapper.addMarket(market);
        return market.getId();
    }

    @Override
    public List<CorgiUserGoods> getUserGoods(CorgiUserGoods goods) {
        if (goods.getStart() == null) {
            goods.setStart(0);
        }
        if (goods.getSize() == null) {
            goods.setSize(1);
        }
        return corgiOrderMapper.getUserGoods(goods);
    }

    @Override
    public List<CorgiUserGoods> getHotGoods(CorgiUserGoods goods) {
        return corgiOrderMapper.getHotGoods(goods);
    }

    @Override
    public void updateUserGoods(CorgiUserGoods goods) {
        corgiOrderMapper.updateUserGoods(goods);
    }

    @Override
    public Double countIncome(CorgiOrder query) {
        Double result = corgiOrderMapper.sumOrder(query);
        return result == null ? 0.0 : result;
    }

    @Override
    public String getUserLocationExpireDate(String userId) {
        String locationExpire = corgiOrderMapper.getLocationExpireDate(userId);
        if (StringUtils.isNotEmpty(locationExpire) && new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                .compareTo(locationExpire) < 0) {
            return locationExpire;
        }
        return "";
    }

    private PushMessage buildBillboardMessage(CorgiUserGoods goods) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(goods.getUserId());
        pushMessage.setMessage("付费人气榜单已购买成功，您的请求，将在一个工作日内，由咱们的运营小哥哥在后台确认后会由系统通知您哦！");
        return pushMessage;
    }

    private PushMessage buildBonusMessage(String userId){
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(userId);
        pushMessage.setMessage("您已成功获得30天Corgi会员奖励！");
        return pushMessage;
    }

    private PushMessage buildInvitedMessage(String userId, String inviteId){
        UserDetail userDetail = corgiUserMapper.getUserDetail(inviteId);
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(userId);
        pushMessage.setMessage("邀请成功通知");
        JSONArray content = new JSONArray();
        content.add(new JSONObject().fluentPut("text", "恭喜你邀请 "+userDetail.getNickname()+" 成功啦"));
        content.add(new JSONObject().fluentPut("text", " 去和他打声招呼吧 >>").fluentPut("url", inviteId).fluentPut("urlType", "5"));
        HashMap<String, Object> extra = new HashMap<>();
        extra.put("type", "907");
        extra.put("content", content);
        pushMessage.setExtra(extra);
        return pushMessage;
    }

    private PushMessage buildSubscribeMessage(CorgiUserGoods goods, int days, String finalDate) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(goods.getUserId());
        pushMessage.setMessage("Corgi会员服务开通成功通知");
        JSONArray content = new JSONArray();
        String yearMember = "";
        if (days > 300) {
            yearMember = "并享有一次上榜体验权益（年费会员专享）,";
        }
        content.add(new JSONObject().fluentPut("text", "Corgi会员服务开通成功通知\n恭喜您已开通 " + days + "天会员服务，" + yearMember + "目前有效期至" + finalDate + "\n更多会员权益可前往"));
        content.add(new JSONObject().fluentPut("text", "会员页面查看 >").fluentPut("urlType", "9"));
        HashMap<String, Object> extra = new HashMap<>();
        extra.put("type", "907");
        extra.put("content", content);
        pushMessage.setExtra(extra);
        return pushMessage;
    }

    private PushMessage buildLocationMonthMessage(CorgiUserGoods goods, String finalDate) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(goods.getUserId());
        pushMessage.setMessage("Corgi定位包月查看服务开通成功通知\n恭喜您已开通 30 天定位包月查看服务，目前有效期至" + finalDate);
        return pushMessage;
    }

    private PushMessage buildWechatMessage(CorgiUserGoods goods) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgi" + goods.getUserId());
        pushMessage.setTargetUserId(goods.getTraderId());
        pushMessage.setMessage("Hey~我已支付" + goods.getPrice() + "元，成功购买了你的微信~很期待与你有更多的认识呢~");
        return pushMessage;
    }

    private PushMessage buildWechatReplyMessage(CorgiUserGoods goods, String reply) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgi" + goods.getTraderId());
        pushMessage.setTargetUserId(goods.getUserId());
        pushMessage.setMessage(reply);
        return pushMessage;
    }

    private PushMessage buildActivityMessage(CorgiUserGoods goods) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(goods.getTraderId());
        pushMessage.setMessage("收益提醒");
        JSONArray content = new JSONArray();
        UserDetail detail = corgiUserMapper.getUserDetail(goods.getUserId());
        content.add(new JSONObject().fluentPut("text", detail.getNickname()).fluentPut("url", detail.getUserId())
                .fluentPut("urlType", "4").fluentPut("isBold", true));
        content.add(new JSONObject().fluentPut("text", " 刚刚支付解锁了你的付费内容"));
        HashMap<String, Object> extra = new HashMap<>();
        extra.put("type", "907");
        extra.put("content", content);
        extra.put("bottomText", "查看收益>");
        extra.put("bottomUrlType", "10");
        extra.put("urlType", "2");
        extra.put("url", goods.getGoodsId());
        List<ActivityPic> pics = corgiPicService.getActivityPic(goods.getGoodsId());
        CorgiActivity activity = corgiActivityFeedService.getActivityById(goods.getGoodsId());
        if (CollectionUtils.isNotEmpty(pics)) {
            extra.put("picUrl", pics.get(0).getPicUrl());
        } else if (StringUtils.isNotEmpty(activity.getCoverUrl())) {
            extra.put("picUrl", activity.getCoverUrl());
        }
        extra.put("title", activity.getTitle() == null ? "" : activity.getTitle());
        extra.put("desc", activity.getContent() == null ? "" : activity.getContent());
        pushMessage.setExtra(extra);
        return pushMessage;
    }

    private void lock(String key) {
        for (int i = 0; i < 100; i++) {
            if (redisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis() + "")) {
                redisTemplate.expire(key, 10l, TimeUnit.SECONDS);
                return;
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void unlock(String key) {
        redisTemplate.delete(key);
    }

}
