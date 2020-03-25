package com.corgi.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.api.CorgiBlackActivityService;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.mapper.*;
import com.corgi.user.api.CorgiBlacklistService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiBlacklistService.class)
@Slf4j
@Component
public class CorgiBlacklistServiceImpl implements CorgiBlacklistService {
    @Autowired
    private CorgiBlacklistMapper corgiBlacklistMapper;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;
    @Autowired
    private CorgiFavorActivityMapper corgiFavorActivityMapper;
    @Autowired
    private CorgiUserActivityMapper corgiUserActivityMapper;
    @Reference
    private CorgiBlackActivityService corgiBlackActivityService;
    @Reference
    private CorgiActivityService corgiActivityService;

    @Override
    public void addBlacklist(String userId, String blackId) {
        corgiBlacklistMapper.addBlacklist(userId, blackId);
        corgiUserFollowMapper.removeFollowUser(userId, blackId);
        corgiUserFollowMapper.removeFollowUser(blackId, userId);
        deleteSignUp(userId, blackId);
        deleteSignUp(blackId, userId);
        corgiBlackActivityService.deleteFavorActivity(userId, blackId);

    }

    @Override
    public void deleteBlacklist(String userId, String blackId) {
        corgiBlacklistMapper.deleteBlacklist(userId, blackId);
    }

    @Override
    public List<UserBasic> getBlackUser(String userId) {
        return corgiBlacklistMapper.getBlacklist(userId);
    }

    @Override
    public void report(CorgiReport report) {
        if (StringUtils.isEmpty(report.getAccuseId())) {
            return;
        }
        if (report.getAccuseType() != null && report.getAccuseType().startsWith("用户")) {
            UserDetail userDetail = corgiUserMapper.getUserDetail(report.getAccuseId());
            if (userDetail != null) {
                report.setAccuseName(StringUtils.isEmpty(userDetail.getCheckNickname()) ? userDetail.getNickname() : userDetail.getCheckNickname());
            }
        } else {
            List<CorgiActivity> corgiActivities = corgiActivityService.getActivityByIds(Arrays.asList(report.getAccuseId()));
            if (!CollectionUtils.isEmpty(corgiActivities)) {
                CorgiActivity activity = corgiActivities.get(0);
                report.setAccuseName(StringUtils.isEmpty(activity.getCheckTitle()) ? activity.getTitle() : activity.getCheckTitle());
            }
        }
        corgiBlacklistMapper.addReport(report);
        if (!CollectionUtils.isEmpty(report.getPics())) {
            for (String url : report.getPics()) {
                corgiBlacklistMapper.addReportPic(report.getId(), url);
            }
        }
    }

    @Override
    public void updateStatus(String reportId, String status) {
        corgiBlacklistMapper.updateReportStatus(reportId, status);
    }

    @Override
    public List<CorgiReport> getReport(CorgiReport report) {
        List<CorgiReport> reports = corgiBlacklistMapper.getReport(report.getReportStatus());
        if (!CollectionUtils.isEmpty(reports)) {
            for (CorgiReport report1 : reports) {
                report1.setPics(corgiBlacklistMapper.getReportPic(report1.getId()));
            }
        }
        return reports;
    }

    private void deleteSignUp(String userId, String blackId) {
        int start = 0;
        int size = 500;
        List<String> activityIds;
        do {
            activityIds = corgiUserActivityMapper.getSignUpActivityId(userId, start, size);
            if (CollectionUtils.isEmpty(activityIds)) {
                break;
            }
            for (String activityId : activityIds) {
                if (corgiBlackActivityService.checkActivity(userId, blackId, activityId)) {
                    UserSignUp userSignUp = new UserSignUp();
                    userSignUp.setUserId(userId);
                    userSignUp.setActivityId(activityId);
                    corgiUserActivityMapper.deleteSignUp(userSignUp);
                }
            }
        } while (activityIds.size() >= size);
    }
}
