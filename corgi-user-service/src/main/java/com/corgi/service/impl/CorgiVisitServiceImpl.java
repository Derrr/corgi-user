package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiVisitMapper;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.api.CorgiVisitService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public void visit(String userId, String toId) {
        if (!userId.equals(toId)) {
            corgiVisitMapper.addVisit(toId, userId);
            corgiVisitMapper.addVisitCount(toId, userId);
        }
    }

    @Override
    public List<UserProfile> getVisitor(String userId, Integer limit) {
        return corgiVisitMapper.getVisitor(userId, limit);
    }

    @Override
    public List<UserProfile> getVisited(String userId, Integer limit) {
        List<UserProfile> profiles = corgiVisitMapper.getVisited(userId, limit);
        String expire = corgiUserService.getUserVipExpire(userId);
        if (!"-".equals(expire)) {
            corgiVisitMapper.readVisit(userId);
        } else {
            UserDetail detail = corgiUserService.getUserDetailBasic(userId);
            if ("influencer".equals(detail.getAvatarStatus())) {
                corgiVisitMapper.readVisit(userId);
            }
        }
        return profiles;
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
}
