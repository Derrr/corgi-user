package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.mapper.CorgiBarMapper;
import com.corgi.mapper.CorgiFakeMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.ActivityLike;
import com.corgi.user.entity.BarProfile;
import com.corgi.user.entity.UserQuery;
import com.corgi.user.entity.UserVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiFakeService.class)
@Slf4j
@Component
public class CorgiFakeServiceImpl implements CorgiFakeService {
    @Autowired
    private CorgiFakeMapper corgiFakeMapper;
    @Reference
    private CorgiUserFollowService corgiUserFollowService;
    @Reference
    private CorgiLikeService corgiLikeService;

    @Override
    public void refreshFakeUser(Integer size) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        corgiFakeMapper.clearFakeUserPool();
        corgiFakeMapper.initFakeUserPool(size, calendar.getTimeInMillis());
    }

    @Override
    public String selectFakeUser() {
        Integer count = corgiFakeMapper.countFakeUser();
        Random random = new Random();
        return corgiFakeMapper.selectFakeUser(random.nextInt(count));
    }

    @Override
    public Boolean addFakeFollower(String userId, String followId) {
        int result = corgiUserFollowService.isFollowed(userId, followId);
        if (result != 1 && result < 3) {
            corgiFakeMapper.addFakeFollower(userId, followId);
            corgiUserFollowService.follow(userId, followId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean addFakeLike(ActivityLike activityLike) {
        Integer result = corgiLikeService.countUserLike(activityLike.getActivityId(), activityLike.getLikeUserId());
        if (result > 0) {
            return false;
        }
        activityLike.setType("fake");
        corgiLikeService.addActivityLike(activityLike);
        return true;
    }

    @Override
    public Integer countFakeFollower(String userId) {
        return corgiFakeMapper.countFakeFollower(userId);
    }

    @Override
    public Integer countFakeLike(String activityId) {
        return corgiFakeMapper.countFakeLike(activityId);
    }

    @Override
    public String getLastActivity(String userId, String time) {
        return corgiFakeMapper.getLastActivity(userId, time);
    }

    @Override
    public void updateFakeTime(String userId) {
        corgiFakeMapper.updateFakeUserPosition(userId, System.currentTimeMillis());
    }

    @Override
    public String getLastFakeFollowTime(String userId) {
        return corgiFakeMapper.getLastFakeFollowTime(userId);
    }
}
