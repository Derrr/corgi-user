package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiDateMapper;
import com.corgi.user.api.CorgiUserDateService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.CorgiApplyCombind;
import com.corgi.user.entity.CorgiDate;
import com.corgi.user.entity.CorgiDateApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserDateService.class)
@Slf4j
@Component
public class CorgiDateServiceImpl implements CorgiUserDateService {
    @Autowired
    private CorgiDateMapper corgiDateMapper;
    @Autowired
    private CorgiUserService corgiUserService;

    @Override
    public void addDate(CorgiDate date) {
        if (StringUtils.isEmpty(date.getStatus())) {
            CorgiDate oldDate = corgiDateMapper.getDateByUserId(date.getUserId());
            if (oldDate == null) {
                date.setStatus(CorgiDate.OPEN);
            } else {
                date.setStatus(oldDate.getStatus());
            }
        }
        corgiDateMapper.addCorgiDate(date);
    }

    @Override
    public List<CorgiDate> searchDate(CorgiDate date) {
        return corgiDateMapper.searchDate(date);
    }

    @Override
    public void updateDate(CorgiDate date) {
        corgiDateMapper.updateCorgiDate(date);
    }

    @Override
    public CorgiDate getDateByUserId(String userId) {
        CorgiDate date = corgiDateMapper.getDateByUserId(userId);
        if (date == null) {
            date = new CorgiDate();
            date.setUserId(userId);
            date.setStatus(CorgiDate.EMPTY);
        }
        return date;
    }

    @Override
    public CorgiDateApply apply(CorgiDateApply apply) {
        apply.setStatus(CorgiDateApply.APPLY);
        return corgiDateMapper.addDateApply(apply);
    }

    @Override
    public CorgiDateApply approve(CorgiDateApply apply) {
        corgiDateMapper.updateDateApply(apply);
        return this.getApplyDetail(apply.getId());
    }

    @Override
    public CorgiDateApply getApplyDetail(Integer id) {
        CorgiDateApply apply = corgiDateMapper.getApplyById(id);
        CorgiDate date = corgiDateMapper.getDateById(apply.getDateId());
        apply.setDateDetail(date);
        return apply;
    }

    @Override
    public List<CorgiDateApply> getApplies(String userId, String startTime, String endTime, String status) {
        List<CorgiDateApply> applies = corgiDateMapper.getApplies(userId, startTime, endTime, status);
        for (CorgiDateApply apply : applies) {
            apply.setUserInfo(corgiUserService.getUserDetailBasic(apply.getApprovalUserId()));
        }
        return applies;
    }

    @Override
    public CorgiApplyCombind getApplyCombind(String userId, String startTime, String status) {
        CorgiApplyCombind combind = corgiDateMapper.getCombind(userId, startTime, status);
        combind.setAvatars(corgiDateMapper.getAvatars(userId, startTime, status));
        return combind;
    }

    @Override
    public CorgiDateApply getUserApply(String userId, String targetUser) {
        CorgiDateApply apply = corgiDateMapper.getUserApply(userId, targetUser);
        apply.setDateDetail(corgiDateMapper.getDateById(apply.getDateId()));
        return apply;
    }

    @Override
    public List<CorgiDateApply> getApprovedApplies(String userId, String status) {
        return null;
    }

}
