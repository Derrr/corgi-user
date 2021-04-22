package com.corgi.user.api;


import com.corgi.user.entity.CorgiApplyCombind;
import com.corgi.user.entity.CorgiDate;
import com.corgi.user.entity.CorgiDateApply;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserDateService {
    void addDate(CorgiDate date);

    List<CorgiDate> searchDate(CorgiDate date);

    void updateDate(CorgiDate date);

    CorgiDate getDateByUserId(String userId);

    CorgiDateApply apply(CorgiDateApply apply);

    CorgiDateApply approve(CorgiDateApply apply);

    CorgiDateApply getApplyDetail(Integer id);

    List<CorgiDateApply> getApplies(String userId, Integer page, Integer pageSize);

    CorgiApplyCombind getApplyCombind(String userId, String startTime, String status);

    CorgiDateApply getUserApply(String userId, String targetUser);

    void updateApply(CorgiDateApply apply);
}
