package com.corgi.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiBlackActivityService;
import com.corgi.mapper.*;
import com.corgi.user.api.CorgiBlacklistService;
import com.corgi.user.entity.UserBasic;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
