package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserActivityMapper;
import com.corgi.user.api.CorgiUserActivityService;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service
@Component
public class CorgiUserActivityServiceImpl implements CorgiUserActivityService {
    @Autowired
    private CorgiUserActivityMapper corgiUserActivityMapper;

    @Override
    public boolean signUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.addSignUp(userSignUp);
        return true;
    }

    @Override
    public boolean updateSignUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.updateSignUp(userSignUp);
        return true;
    }

    @Override
    public List<UserProfile> getUsers(String activityId) {
        return corgiUserActivityMapper.getUser(activityId);
    }
}
