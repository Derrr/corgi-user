package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.activity.entity.CorgiActivity;
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
    public void deleteSignUpByActivity(String activityId) {
        corgiUserActivityMapper.deleteSignUpByActivity(activityId);
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
    public List<UserProfile> getPopularUsers(String activityId, String status) {
        List<UserProfile> userProfiles = corgiUserActivityMapper.getPopularUser(activityId, status);
        return corgiUserService.populateUserProfile(userProfiles, null);
    }

    @Override
    public Integer countUsers(String activityId, String status) {
        return corgiUserActivityMapper.countUser(activityId, status);
    }

    @Override
    public List<String> getSignUpActivity(String userId, Integer page, Integer pageSize) {
        return corgiUserActivityMapper.getSignUpActivityId(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<String> getHeatActivity(CorgiActivity corgiActivity, Integer page, Integer pageSize) {
        String category;
        if (corgiActivity.getBarId() != null) {
            category = corgiActivity.getCategory() + corgiActivity.getBarId();
        } else {
            category = "video','image";
        }
        String date = corgiActivity.getCreateTime();
        List<String> topics = corgiActivity.getTopics();
        String topic = null;
        if (!CollectionUtils.isEmpty(topics)) {
            topic = topics.get(0);
        }
        return corgiUserActivityMapper.getHeadActivityPic(category, date, (page - 1) * pageSize, pageSize, topic);
    }

    @Override
    public void addActivityCreator(String userId, String activityId, String category) {
        corgiUserActivityMapper.addActivityCreator(activityId, userId, category);
    }

    @Override
    public void failActivityCreator(String activityId) {
        corgiUserActivityMapper.failActivityCreator(activityId);
    }

    @Override
    public long countActivity(String date, String category) {
        return corgiUserActivityMapper.countActivity(category, date);
    }

    @Override
    public long countActivityUser(String date, String category) {
        return corgiUserActivityMapper.countActivityUser(category, date);
    }

    @Override
    public long countSignUpUser(String date) {
        return corgiUserActivityMapper.countSignUpUser(date);
    }

    @Override
    public void deleteActivityCreator(String activityId) {
        corgiUserActivityMapper.deleteActivityCreator(activityId);
    }

    @Override
    public List<String> getParticipateActivity(String userId, Integer page, Integer size) {
        return corgiUserActivityMapper.getParticipateActivity(userId, (page - 1) * size, size);
    }
}
