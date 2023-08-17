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

    void addLog(String result, String transationId, String originId);

    List<CorgiOrder> getOrderByPage(CorgiOrder order, Integer page, Integer pageSize);

    Integer countOrder(CorgiOrder order);

    CorgiOrder getOrderByTradeNo(String tradeNo);

    CorgiMerchandise getMerchandiseById(String merchId, String userId);

    String buy(String tradeNo, String merchId, String expiresDate);

    void subscribe(CorgiOrder order, CorgiUserGoods goods, String vipStatus, String finalDate);

    void updateReceipt(String tradeNo, String receipt);

    String getReceipt(String userId, String receipt);

    String addUserMarket(CorgiUserMarket market);

    List<CorgiUserGoods> getUserGoods(CorgiUserGoods goods);

    List<CorgiUserGoods> getHotGoods(CorgiUserGoods goods);

    void updateUserGoods(CorgiUserGoods goods);

    Double countIncome(CorgiOrder query);
}
