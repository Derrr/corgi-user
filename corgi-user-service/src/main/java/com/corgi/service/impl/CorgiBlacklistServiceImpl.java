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
    @Autowired
    private CorgiPicMapper corgiPicMapper;
    @Reference
    private CorgiBlackActivityService corgiBlackActivityService;
    @Reference
    private CorgiActivityService corgiActivityService;


    @Override
    public String addBlacklist(String userId, String blackId) {
        Integer result = corgiBlacklistMapper.addBlacklist(userId, blackId);
        if (result > 0) {
            corgiUserFollowMapper.removeFollowUser(userId, blackId);
            corgiUserFollowMapper.removeFollowUser(blackId, userId);
            deleteSignUp(userId, blackId);
            deleteSignUp(blackId, userId);
            corgiBlackActivityService.deleteFavorActivity(userId, blackId);
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public String deleteBlacklist(String userId, String blackId) {
        Integer result = corgiBlacklistMapper.deleteBlacklist(userId, blackId);
        if (result > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public List<UserBasic> getBlackUser(String userId) {
        List<UserBasic> userBasics = corgiBlacklistMapper.getBlacklist(userId);
//        if (userBasics != null) {
//            for (UserBasic userBasic : userBasics) {
//                userBasic.setUserPics(corgiPicMapper.getUserPic(userBasic.getUserId()));
//            }
//        }
        return userBasics;
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
    public void updateStatus(String reportId, String status, String result) {
        corgiBlacklistMapper.updateReportStatus(reportId, status, result);
    }

    @Override
    public List<CorgiReport> getReport(CorgiReport report, Integer page, Integer size) {
        List<CorgiReport> reports = corgiBlacklistMapper.getReport(report, (page - 1) * size, size);
        if (!CollectionUtils.isEmpty(reports)) {
            for (CorgiReport report1 : reports) {
                report1.setPics(corgiBlacklistMapper.getReportPic(report1.getId()));
                CorgiReport query = new CorgiReport();
                query.setAccuseId(report1.getAccuseUserId());
                report1.setAccuseTimes(corgiBlacklistMapper.countReport(query));
                report1.setReporterCount(corgiBlacklistMapper.countReportUser(query));
            }
        }
        return reports;
    }

    @Override
    public Integer countReport(CorgiReport report) {
        return corgiBlacklistMapper.countReport(report);
    }

    @Override
    public List<String> getBeBlacked(String userId) {
        return corgiBlacklistMapper.getBeBlacklist(userId);
    }

    @Override
    public Integer isBlacked(String userId, String targetUserId) {
        return corgiBlacklistMapper.countBlack(userId, targetUserId) + 2 * corgiBlacklistMapper.countBlack(targetUserId, userId);
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
