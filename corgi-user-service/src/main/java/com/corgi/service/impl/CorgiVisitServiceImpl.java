package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiVisitMapper;
import com.corgi.user.api.CorgiUserFollowService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.api.CorgiVisitService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiVisitService.class)
@Slf4j
@Component
public class CorgiVisitServiceImpl implements CorgiVisitService {
    @Autowired
    private CorgiVisitMapper corgiVisitMapper;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiUserFollowService corgiUserFollowService;

    @Override
    public void visit(String userId, String toId) {
        if (!userId.equals(toId)) {
            corgiVisitMapper.addVisit(toId, userId);
            corgiVisitMapper.addVisitCount(toId, userId);
        }
    }

    @Override
    public List<UserProfile> getVisitor(String userId, Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        List<UserProfile> profiles = corgiVisitMapper.getVisitor(userId, (page - 1) * limit, limit);
        String expire = corgiUserService.getUserVipExpire(userId);
        if (!"-".equals(expire)) {
            corgiVisitMapper.readVisit(userId);
        } else {
            UserDetail detail = corgiUserService.getUserDetailBasic(userId);
            if (UserDetail.INFLUENCER.equals(detail.getAvatarStatus())) {
                corgiVisitMapper.readVisit(userId);
            }
        }
        return convert(profiles, userId);
    }

    @Override
    public List<UserProfile> getVisited(String userId, Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        List<UserProfile> profiles = corgiVisitMapper.getVisited(userId, (page - 1) * limit, limit);
        return convert(profiles, userId);
    }

    @Override
    public List<UserProfile> getVisitedByCount(String userId, Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        List<UserProfile> profiles = corgiVisitMapper.getVisitedByCount(userId, (page - 1) * limit, limit);
        return convert(profiles, userId);
    }

    @Override
    public List<UserProfile> getVisitorByCount(String userId, Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        List<UserProfile> profiles = corgiVisitMapper.getVisitorByCount(userId, (page - 1) * limit, limit);
        return convert(profiles, userId);
    }

    @Override
    public Integer countVisit(String userId) {
        Integer result = corgiVisitMapper.countVisit(userId);
        if (result == null) {
            result = 0;
        }
        return result;
    }

    @Override
    public Integer countVisitUnread(String userId) {
        Integer result = corgiVisitMapper.countVisitUnread(userId);
        if (result == null) {
            result = 0;
        }
        return result;
    }

    List<UserProfile> convert(List<UserProfile> profiles, String userId) {
        if (CollectionUtils.isEmpty(profiles)) {
            return new ArrayList<>();
        }
        for (UserProfile profile : profiles) {
            profile.setIsFollowed(corgiUserFollowService.isFollowed(userId, profile.getUserId()));
        }
        return profiles;
    }
}
