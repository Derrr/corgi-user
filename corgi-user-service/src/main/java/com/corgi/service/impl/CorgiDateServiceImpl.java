package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiDateMapper;
import com.corgi.user.api.CorgiUserDateService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.CorgiApplyCombind;
import com.corgi.user.entity.CorgiDate;
import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserDetail;
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
        CorgiDate update = new CorgiDate();
        update.setStatus(CorgiDate.CLOSE);
        update.setUserId(date.getUserId());
        corgiDateMapper.updateAllCorgiDate(update);
        corgiDateMapper.addCorgiDate(date);
    }

    @Override
    public List<CorgiDate> searchDate(CorgiDate date) {
        return corgiDateMapper.searchDate(date);
    }

    @Override
    public void updateDate(CorgiDate date) {
        if (CorgiDate.OPEN.equals(date.getStatus())) {
            corgiDateMapper.updateCorgiDate(date);
        } else if (CorgiDate.CLOSE.equals(date.getStatus())) {
            corgiDateMapper.updateAllCorgiDate(date);
        }
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
        CorgiDateApply corgiDateApply = corgiDateMapper.getUserApply(apply.getApprovalUserId(), apply.getApplyUserId());
        if (corgiDateApply != null && CorgiDateApply.APPLY.equals(corgiDateApply.getStatus())) {
            apply.setStatus("exists");
            return apply;
        }
        CorgiDate date = corgiDateMapper.getDateByUserId(apply.getApprovalUserId());
        if (date == null || CorgiDate.CLOSE.equals(date.getStatus())) {
            return apply;
        }
        apply.setDateId(date.getId() + "");
        corgiDateMapper.addDateApply(apply, date);
        return apply;
    }

    @Override
    public CorgiDateApply approve(CorgiDateApply apply) {
        corgiDateMapper.updateDateApply(apply);
        return this.getApplyDetail(apply.getId());
    }

    @Override
    public CorgiDateApply getApplyDetail(Integer id) {
        CorgiDateApply apply = corgiDateMapper.getApplyById(id);
        return apply;
    }

    @Override
    public List<CorgiDateApply> getApplies(String userId, Integer page, Integer pageSize) {
        List<CorgiDateApply> applies = corgiDateMapper.getApplies(userId, (page - 1) * pageSize, pageSize);
        for (CorgiDateApply apply : applies) {
            this.setUserInfo(apply, userId);
        }
        return applies;
    }

    @Override
    public List<CorgiDateApply> searchApplies(CorgiDateApply apply, Integer page, Integer pageSize) {
        return corgiDateMapper.searchApplies(apply, (page - 1) * pageSize, pageSize);
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
        return apply;
    }

    @Override
    public void updateApply(CorgiDateApply apply) {
        corgiDateMapper.updateDateApply(apply);
    }

    private void setUserInfo(CorgiDateApply apply, String userId) {
        String resultId = apply.getApplyUserId();
        if (userId.equals(resultId)) {
            resultId = apply.getApprovalUserId();
        }
        UserDetail userDetail = corgiUserService.getUserDetailBasic(resultId);
        if (userDetail == null) {
            userDetail = new UserDetail();
            userDetail.setUserId(resultId);
            userDetail.setNickname("已注销");
        }
        apply.setUserInfo(userDetail);
    }

}
