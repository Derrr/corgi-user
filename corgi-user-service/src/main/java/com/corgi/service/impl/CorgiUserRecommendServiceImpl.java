package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserRecommendMapper;
import com.corgi.user.api.CorgiUserRecommendService;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserRecommendService.class)
@Slf4j
@Component
public class CorgiUserRecommendServiceImpl implements CorgiUserRecommendService {
    @Autowired
    private CorgiUserRecommendMapper corgiUserRecommendMapper;


    @Override
    public void clearRecUser(String userId) {
        corgiUserRecommendMapper.clearRecUsers(userId);
    }

    @Override
    public void followRecUser(String userId, String recId) {
        corgiUserRecommendMapper.insertUserRecommend(userId, recId);
        corgiUserRecommendMapper.updateStatus(userId, recId, "1", "0");
    }

    @Override
    public void addRecUser(String userId, String recId) {
        corgiUserRecommendMapper.insertUserRecommend(userId, recId);
        corgiUserRecommendMapper.addRecommend(userId, recId);
    }

    @Override
    public void updateRecStatus(String userId, String recId, String status) {
        corgiUserRecommendMapper.updateStatus(userId, recId, status, System.currentTimeMillis() + "");
    }

    @Override
    public List<UserProfile> getRecUser(String userId, Integer page, Integer size) {
        return corgiUserRecommendMapper.getRecUsers(userId, (page - 1) * size, size);
    }

    @Override
    public void deleteRecUserByWeight(String userId, Integer weight) {

    }
}
