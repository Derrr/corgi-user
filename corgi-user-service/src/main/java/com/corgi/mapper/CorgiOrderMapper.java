package com.corgi.mapper;

import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;
import com.corgi.user.entity.CorgiUserGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiOrderMapper {
    List<CorgiMerchandise> searchMerchandise(@Param("merchandise") CorgiMerchandise merchandise);

    CorgiMerchandise getMerchandiseById(@Param("merchId") String merchId);

    void addLog(@Param("order") CorgiOrder order);

    void addOrder(@Param("order") CorgiOrder order);

    void updateOrder(@Param("order") CorgiOrder order);

    List<CorgiOrder> getOrderByPage(@Param("order") CorgiOrder order, @Param("start")Integer start, @Param("size")Integer size);

    CorgiOrder getOrderByTradeNo(@Param("tradeNo")String tradeNo);

    void addGoods(@Param("goods") CorgiUserGoods goods);
}
