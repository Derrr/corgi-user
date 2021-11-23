package com.corgi.user.api;


import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;
import com.corgi.user.entity.CorgiUserGoods;
import com.corgi.user.entity.CorgiUserMarket;

import java.util.List;

public interface CorgiOrderService {
    List<CorgiMerchandise> getMerchandise(CorgiMerchandise merchandise);

    void addOrder(CorgiOrder order);

    void updateOrder(CorgiOrder order);

    List<CorgiOrder> getOrderByPage(CorgiOrder order, Integer page, Integer pageSize);

    CorgiOrder getOrderByTradeNo(String tradeNo);

    CorgiMerchandise getMerchandiseById(String merchId);

    String buy(String tradeNo);

    void subscribe(CorgiOrder order, CorgiUserGoods goods, String vipStatus, String finalDate);

    void updateReceipt(String tradeNo, String receipt);

    String getReceipt(String receipt);

    String addUserMarket(CorgiUserMarket market);

    List<CorgiUserGoods> getUserGoods(CorgiUserGoods goods);
}
