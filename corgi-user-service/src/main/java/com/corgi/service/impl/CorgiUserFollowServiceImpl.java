package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserFollowMapper;
import com.corgi.user.api.CorgiUserFollowService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserFollowService.class)
@Component
public class CorgiUserFollowServiceImpl implements CorgiUserFollowService {
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;
    @Autowired
    private CorgiUserService corgiUserService;

    @Override
    public boolean follow(String userId, String followUserId) {
        corgiUserFollowMapper.addFollowUser(userId, followUserId);
        return true;
    }

    @Override
    public boolean unfollow(String userId, String unFollowUserId) {
        corgiUserFollowMapper.deleteFollowUser(userId, unFollowUserId);
        return true;
    }

    @Override
    public int isFollowed(String userId, String targetUserId) {
        return corgiUserFollowMapper.countFollow(userId, targetUserId) + corgiUserFollowMapper.countFollow(targetUserId, userId) * 2;
    }

    @Override
    public List<String> getFollowUser(String userId) {
        return corgiUserFollowMapper.getFollowUser(userId);
    }

    @Override
    public List<UserProfile> getFollowUserByPage(String userId, String type, Double lat, Double lng, Integer page, Integer pageSize) {
        List<UserProfile> userProfiles = corgiUserFollowMapper.getFollowUserByPage(userId, type, lat, lng, (page - 1) * pageSize, pageSize);
        return corgiUserService.populateUserProfile(userProfiles, userId);
    }

    @Override
    public List<UserProfile> getFollowedUserByPage(String userId, long time, Integer page, Integer pageSize) {
        return corgiUserFollowMapper.getFollowedUserByPage(userId, (page - 1) * pageSize, pageSize);
    }
}
