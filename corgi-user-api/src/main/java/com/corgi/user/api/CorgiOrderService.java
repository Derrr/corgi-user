package com.corgi.user.api;


import com.corgi.user.entity.CorgiMerchandise;
import com.corgi.user.entity.CorgiOrder;

import java.util.List;

public interface CorgiOrderService {
    List<CorgiMerchandise> getMerchandise(CorgiMerchandise merchandise);

    void addOrder(CorgiOrder order);

    void updateOrder(CorgiOrder order);

    List<CorgiOrder> getOrderByPage(CorgiOrder order, Integer page, Integer pageSize);

    CorgiMerchandise getMerchandiseById(String merchId);
}
