package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiLikeMapper;
import com.corgi.user.api.CorgiLikeService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.ActivityLike;
import com.corgi.user.entity.ActivityMessage;
import com.corgi.user.entity.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Service(interfaceClass = CorgiLikeService.class)
@Slf4j
@Component
public class CorgiLikeServiceImpl implements CorgiLikeService {
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiLikeMapper corgiLikeMapper;
    @Autowired
    private CorgiToolService corgiToolService;

    @Override
    public void addActivityLike(ActivityLike activityLike) {
        UserDetail userDetail = corgiUserService.getUserDetail(activityLike.getLikeUserId(), null);
        activityLike.setLikeUserName(userDetail.getNickname());
        activityLike.setLikeUserAvatar(userDetail.getUserPics().get(0).getPicUrl());

        corgiLikeMapper.addActivityLike(activityLike);
        corgiToolService.addActivityMessage(ActivityMessage.builder()
                .activityId(activityLike.getActivityId())
                .fromUserAvatar(userDetail.getUserPics().get(0).getPicUrl())
                .fromUserId(userDetail.getUserId())
                .fromUserName(userDetail.getNickname())
                .toUserId(activityLike.getUserId())
                .time(System.currentTimeMillis())
                .messageType(ActivityMessage.LIKE)
                .build());
    }

    @Override
    public List<ActivityLike> getActivityLike(String activityId) {
        return corgiLikeMapper.getActivityLike(activityId);
    }

    @Override
    public Long countActivityLike(String activityId) {
        return corgiLikeMapper.countActivityLike(activityId);
    }
}
