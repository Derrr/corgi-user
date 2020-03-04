package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CorgiTopic;
import com.corgi.mapper.CorgiPicMapper;
import com.corgi.mapper.CorgiUserActivityMapper;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserActivityService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tairanliu
 */
@Service
@Component
public class CorgiUserActivityServiceImpl implements CorgiUserActivityService {
    @Autowired
    private CorgiUserActivityMapper corgiUserActivityMapper;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private CorgiToolService corgiToolService;

    @Override
    public boolean signUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.addSignUp(userSignUp);
        return true;
    }

    @Override
    public boolean signOut(UserSignUp userSignUp) {
        corgiUserActivityMapper.deleteSignUp(userSignUp);
        return true;
    }

    @Override
    public boolean updateSignUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.updateSignUp(userSignUp);
        return true;
    }

    @Override
    public void deleteActivity(String activityId) {
        corgiUserActivityMapper.deleteSignUpByActivity(activityId);
        List<ActivityPic> pics = corgiPicService.getActivityPic(activityId);
        if (!CollectionUtils.isEmpty(pics)) {
            for (ActivityPic activity : pics) {
                corgiPicService.deleteActivityPic(activity.getPicId());
            }
        }
        corgiToolService.updateActivityTopic(activityId, null);
    }

    @Override
    public Integer getStatus(String userId, String activityId) {
        return corgiUserActivityMapper.getStatus(userId, activityId);
    }

    @Override
    public List<UserProfile> getUsers(String activityId, String userId, String status) {
        List<UserProfile> userProfiles = corgiUserActivityMapper.getUser(activityId, status);
        return corgiUserService.populateUserProfile(userProfiles, userId);
    }

    @Override
    public List<String> getSignUpActivity(String userId, Integer page, Integer pageSize) {
        return corgiUserActivityMapper.getSignUpActivityId(userId, (page - 1) * pageSize, pageSize);
    }
}
