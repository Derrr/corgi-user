package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiOrderMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;
import com.corgi.user.entity.CorgiUserGoods;
import com.corgi.user.enums.MerchandiseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        corgiOrderMapper.updateOrder(order);
        corgiOrderMapper.addLog(order);
        if (CorgiOrder.STATUS.SUCCESS.equals(order.getStatus())) {
            this.buy(order.getTradeNo());
        }
    }

    @Override
    public List<CorgiOrder> getOrderByPage(CorgiOrder order, Integer page, Integer pageSize) {
        return corgiOrderMapper.getOrderByPage(order, (page - 1) * pageSize, pageSize);
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
        CorgiOrder order = corgiOrderMapper.getOrderByTradeNo(tradeNo);
        CorgiUserGoods goods = CorgiUserGoods.builder()
                .userId(order.getUserId())
                .currency(CorgiUserGoods.CURRENCY.CNY)
                .price(order.getPayAmount())
                .tradeNo(order.getTradeNo())
                .build();
        CorgiMerchandise merchandise = corgiOrderMapper.getMerchandiseById(order.getMerchId());
        if (CorgiMerchandise.SUBSCRIBE.equals(merchandise.getType())) {
            goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.SUBSCRIBE);
            goods.setGoodsId(merchandise.getId());
            goods.setTraderId("corgi");
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
                calendar.add(Calendar.MONTH, e.getMonths());
                String finalDate = sdf.format(calendar.getTime());
                corgiUserMapper.updateVipExpire(order.getUserId(), "1", finalDate);
                goods.setDesc("购买成功，日期截止至 " + finalDate);
                corgiOrderMapper.addGoods(goods);
            } else {
                order.setResult("merchandise can not be found");
                corgiOrderMapper.addLog(order);
            }

        }
        return null;
    }
}
