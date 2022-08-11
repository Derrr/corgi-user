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
import com.corgi.mapper.CorgiOrderMapper;
import com.corgi.mapper.CorgiReservationMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.api.CorgiPicService;

import com.corgi.user.entity.*;
import com.corgi.user.enums.MerchandiseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private CorgiReservationMapper corgiReservationMapper;
    @Autowired
    private CorgiOrderMapper corgiOrderMapper;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
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
                this.buy(order.getTradeNo(), expiresDate);
            }
        } catch (Exception e) {
            order.setResult(e.getMessage());
            corgiOrderMapper.addLog(order);
        }
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
    public String buy(String tradeNo, String expiresDate) {
        String key = "buying_goods_" + tradeNo;
        try {
            this.lock(key);
            CorgiOrder order = corgiOrderMapper.getOrderByTradeNo(tradeNo);
            if (corgiOrderMapper.countGoodsByTradeNo(tradeNo) > 0) {
                return null;
            }
            CorgiUserGoods goods = CorgiUserGoods.builder()
                    .userId(order.getUserId())
                    .currency(CorgiUserGoods.CURRENCY.CNY)
                    .price(order.getPayAmount())
                    .tradeNo(order.getTradeNo())
                    .merchId(order.getMerchId())
                    .build();
            CorgiMerchandise merchandise = corgiOrderMapper.getMerchandiseById(order.getMerchId());
            if (CorgiMerchandise.SUBSCRIBE.equals(merchandise.getType())) {
                if (!this.buySubscribe(goods, merchandise, order, expiresDate)) {
                    return null;
                }
            } else if (CorgiMerchandise.ACTIVITY.equals(merchandise.getType())) {
                this.buyActivity(goods, order);
            } else if (CorgiMerchandise.BILLBOARD.equals(merchandise.getType())) {
                this.buyBillboard(goods, order, merchandise);
            } else if (CorgiMerchandise.RESERVE.equals(merchandise.getType())) {
                this.buyReserve(goods, order);
            } else {
                this.buyGoods(goods, merchandise);
            }
        } finally {
            this.unlock(key);
        }
        return null;
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
            goods.setDesc("购买成功");
            goods.setMarketId(market.getId());
            goods.setTraderId(market.getUserId());
            corgiOrderMapper.addGoods(goods);
            rabbitTemplate.convertAndSend(CorgiQueueName.PUSH_MESSAGE_QUEUE, this.buildActivityMessage(goods));
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
        MerchandiseEnum e = MerchandiseEnum.getByCode(merchandise.getId());
        if (e != null) {
            String finalDate = "";
            if (StringUtils.isNotEmpty(expiresDate)) {
                if (expiresDate.equals(expireDate.getTime() + "")) {
                    CorgiOrder update = new CorgiOrder();
                    update.setTradeNo(order.getTradeNo());
                    update.setStatus(CorgiOrder.STATUS.CLOSE);
                    corgiOrderMapper.updateOrder(update);
                    return false;
                }
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
            } else {
                order.setTradeNo(order.getUserId());
                order.setResult(finalDate);
                corgiOrderMapper.addLog(order);
            }
            if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                if (goods != null) {
                    corgiOrderMapper.addGoods(goods);
                }
                corgiUserMapper.updateVipExpire(order.getUserId(), "1", finalDate);
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
        return corgiOrderMapper.getUserGoods(goods);
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

    private PushMessage buildSubscribeMessage(CorgiUserGoods goods, int days, String finalDate) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setSourceUserId("corgihelper");
        pushMessage.setTargetUserId(goods.getUserId());
        pushMessage.setMessage("Corgi会员服务开通成功通知");
        JSONArray content = new JSONArray();
        content.add(new JSONObject().fluentPut("text", "Corgi会员服务开通成功通知\n恭喜您已开通 " + days + "天会员服务，目前有效期至" + finalDate + "\n更多会员权益可前往"));
        content.add(new JSONObject().fluentPut("text", "会员页面查看 >").fluentPut("urlType", "9"));
        HashMap<String, Object> extra = new HashMap<>();
        extra.put("type", "907");
        extra.put("content", content);
        pushMessage.setExtra(extra);
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
