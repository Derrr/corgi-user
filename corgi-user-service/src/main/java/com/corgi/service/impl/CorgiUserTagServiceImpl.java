package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserTagMapper;
import com.corgi.user.api.CorgiUserTagService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserTagService.class)
@Slf4j
@Component
public class CorgiUserTagServiceImpl implements CorgiUserTagService {
    @Autowired
    private CorgiUserTagMapper corgiUserTagMapper;

    @Override
    public List<String> getTags() {
        return corgiUserTagMapper.getTags();
    }

    @Override
    public List<String> getUserTags(String userId) {
        return corgiUserTagMapper.getUserTag(userId);
    }

    @Override
    public List<String> getInterestsByCategory(String category) {
        return corgiUserTagMapper.getCategoryInterests(category);
    }

    @Override
    public List<UserInterest> getUserInterest(String userId) {
        return corgiUserTagMapper.getUserInterests(userId);
    }

    @Override
    public void updateUserTag(String userId, List<String> tags) {
        corgiUserTagMapper.deleteUserTag(userId);
        for (String tag : tags) {
            corgiUserTagMapper.addUserTag(userId, tag);
        }
    }

    @Override
    public void updateUserInterest(String userId, String category, List<String> interests) {
        corgiUserTagMapper.deleteUserInterests(userId, category);
        for (String interest : interests) {
            corgiUserTagMapper.addUserInterests(userId, category, interest);
        }
    }
}
