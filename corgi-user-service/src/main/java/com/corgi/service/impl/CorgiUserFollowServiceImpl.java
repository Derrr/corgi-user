package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserFollowMapper;
import com.corgi.user.api.CorgiUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserFollowService.class)
@Component
public class CorgiUserFollowServiceImpl implements CorgiUserFollowService {
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;

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
}
