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
    public void addRecUser(String userId, String recId, Integer weight) {
        if (weight == null) {
            weight = 1;
        }
        corgiUserRecommendMapper.insertUserRecommend(userId, recId);
        corgiUserRecommendMapper.addRecommend(userId, recId, weight);
    }

    @Override
    public void updateRecStatus(String userId, String recId, String status) {
        corgiUserRecommendMapper.updateStatus(userId, recId, status, System.currentTimeMillis() + "");
    }

    @Override
    public List<UserProfile> getRecUser(String userId, Integer size) {
        return corgiUserRecommendMapper.getRecUsers(userId, size);
    }

    @Override
    public List<UserProfile> getInfluencerByCity(String userId, String city, Integer size) {
        List<UserProfile> userProfiles = corgiUserRecommendMapper.getCityInfluencer(city, userId, size);
        if (userProfiles.size() < size) {
            List<UserProfile> nationalProfiles = corgiUserRecommendMapper.getNotCityInfluencer(city, userId, size - userProfiles.size());
            userProfiles.addAll(nationalProfiles);
        }
        return userProfiles;
    }

    @Override
    public List<UserProfile> getCityPopulate(String userId, String city, Integer page, Integer size) {
        return corgiUserRecommendMapper.getCityPopulate(city, userId, (page - 1) * size, size);
    }

    @Override
    public void deleteRecUserByWeight(String userId, Integer weight) {
        corgiUserRecommendMapper.deleteByWeight(userId, weight);
    }

    @Override
    public void distLikeUser(String userId, String disLikeUserId) {
        corgiUserRecommendMapper.insertUserRecommend(userId, disLikeUserId);
        corgiUserRecommendMapper.updateStatus(userId, disLikeUserId, "2", System.currentTimeMillis() + "");
    }

    @Override
    public void initInfluencer() {
        corgiUserRecommendMapper.clearInfluencerBillboard();
        corgiUserRecommendMapper.initInfluencerBillboard();
    }

    @Override
    public List<String> getCityRecommendImage(String userId, String city, Integer page, Integer pageSize) {
        return corgiUserRecommendMapper.getCityImage(city, userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<String> getNotCityRecommendImage(String userId, String city, Integer page, Integer pageSize) {
        return corgiUserRecommendMapper.getNotCityImage(city, userId, (page - 1) * pageSize, pageSize);
    }

}
