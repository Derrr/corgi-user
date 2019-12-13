package com.corgi.service.impl;

import com.corgi.mapper.CorgiUserFollowMapper;
import com.corgi.user.api.CorgiUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tairanliu
 */
@Service
public class CorgiUserFollowServiceImpl implements CorgiUserFollowService {
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;

    @Override
    public boolean follow(String userId, String followUserId) {
        corgiUserFollowMapper.addFollowUser(userId, followUserId);
        return true;
    }

    @Override
    public boolean unFollow(String userId, String unFollowUserId) {
        corgiUserFollowMapper.deleteFollowUser(userId, unFollowUserId);
        return true;
    }

    @Override
    public int isFollowed(String userId, String targetUserId) {
        return corgiUserFollowMapper.countFollow(userId, targetUserId) + corgiUserFollowMapper.countFollow(targetUserId, userId) * 2;
    }
}
