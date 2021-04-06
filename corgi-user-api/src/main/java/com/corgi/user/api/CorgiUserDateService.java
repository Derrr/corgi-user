package com.corgi.user.api;


import com.corgi.user.entity.CorgiDate;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserDateService {
    void addDate(CorgiDate date);

    List<CorgiDate> searchDate(CorgiDate date);

    void updateDate(CorgiDate date);

    CorgiDate getDateByUserId(String userId);
}
