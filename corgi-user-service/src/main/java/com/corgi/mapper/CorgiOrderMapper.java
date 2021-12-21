package com.corgi.mapper;

import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;
import com.corgi.user.entity.CorgiUserGoods;
import com.corgi.user.entity.CorgiUserMarket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiOrderMapper {
    List<CorgiMerchandise> searchMerchandise(@Param("merchandise") CorgiMerchandise merchandise);

    CorgiMerchandise getMerchandiseById(@Param("merchId") String merchId);

    List<CorgiMerchandise> getMerchandiseByAppMerchId(@Param("merchId") String merchId);

    void addLog(@Param("order") CorgiOrder order);

    void addOrder(@Param("order") CorgiOrder order);

    List<CorgiOrder> getOrderByOrderId(@Param("orderId")String orderId);

    void updateOrder(@Param("order") CorgiOrder order);

    List<CorgiOrder> getOrderByPage(@Param("order") CorgiOrder order, @Param("start") Integer start, @Param("size") Integer size);

    Integer countOrder(@Param("order") CorgiOrder order);

    CorgiOrder getOrderByTradeNo(@Param("tradeNo") String tradeNo);

    void addGoods(@Param("goods") CorgiUserGoods goods);

    Integer countGoodsByTradeNo(@Param("tradeNo") String goods);

    void updateReceipt(@Param("tradeNo") String tradeNo, @Param("receipt") String receipt);

    String getReceipt(@Param("receipt") String receipt);

    void addMarket(@Param("market") CorgiUserMarket market);

    List<CorgiUserGoods> getUserGoods(@Param("goods") CorgiUserGoods goods);

    CorgiUserMarket getMarketById(@Param("marketId") String marketId);

    Double sumOrder(@Param("order") CorgiOrder order);
}
