package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiOrderMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;
import com.corgi.user.entity.CorgiUserGoods;
import com.corgi.user.entity.CorgiUserMarket;
import com.corgi.user.enums.MerchandiseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiOrderService.class)
@Slf4j
@Component
public class CorgiOrderServiceImpl implements CorgiOrderService {

    @Autowired
    private CorgiOrderMapper corgiOrderMapper;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

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
            corgiOrderMapper.updateOrder(order);
            corgiOrderMapper.addLog(order);
            if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                this.buy(order.getTradeNo());
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
    public CorgiMerchandise getMerchandiseById(String merchId) {
        return corgiOrderMapper.getMerchandiseById(merchId);
    }

    @Override
    public String buy(String tradeNo) {
        String key = "buying_goods_" + tradeNo;
        this.lock(key);
        try {
            CorgiOrder order = corgiOrderMapper.getOrderByTradeNo(tradeNo);
            if (corgiOrderMapper.countGoodsByTradeNo(tradeNo) > 0) {
                return null;
            }
            CorgiUserGoods goods = CorgiUserGoods.builder()
                    .userId(order.getUserId())
                    .currency(CorgiUserGoods.CURRENCY.CNY)
                    .price(order.getPayAmount())
                    .tradeNo(order.getTradeNo())
                    .traderId(order.getSellerId())
                    .merchId(order.getMerchId())
                    .build();
            CorgiMerchandise merchandise = corgiOrderMapper.getMerchandiseById(order.getMerchId());
            if (CorgiMerchandise.SUBSCRIBE.equals(merchandise.getType())) {
                goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.SUBSCRIBE);
                goods.setGoodsId(merchandise.getId());
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
                    calendar.setTime(expireDate);
                    calendar.add(Calendar.DATE, e.getDays());
                    String finalDate = sdf.format(calendar.getTime());
                    corgiUserMapper.updateVipExpire(order.getUserId(), "1", finalDate);
                    goods.setDesc("购买成功，日期截止至 " + finalDate);
                    corgiOrderMapper.addGoods(goods);
                } else {
                    order.setResult("merchandise can not be found");
                    corgiOrderMapper.addLog(order);
                }
            } else if (CorgiMerchandise.ACTIVITY.equals(merchandise.getType())) {
                CorgiUserMarket market = corgiOrderMapper.getMarketById(order.getMarketId());
                if (market != null) {
                    goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.ACTIVITY);
                    goods.setGoodsId(market.getSourceId());
                    goods.setDesc("购买成功");
                    goods.setTraderId(market.getUserId());
                    corgiOrderMapper.addGoods(goods);
                } else {
                    order.setResult("user market can not be found");
                    corgiOrderMapper.addLog(order);
                }
            }
        } finally {
            this.unlock(key);
        }
        return null;
    }

    @Override
    public void subscribe(CorgiOrder order, CorgiUserGoods goods, String vipStatus, String finalDate) {
        if ("1".equals(vipStatus)) {
            corgiOrderMapper.addOrder(order);
            corgiOrderMapper.updateOrder(order);
            corgiOrderMapper.addLog(order);
            if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
                corgiOrderMapper.addGoods(goods);
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
    public String getReceipt(String receipt) {
        return corgiOrderMapper.getReceipt(receipt);
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
