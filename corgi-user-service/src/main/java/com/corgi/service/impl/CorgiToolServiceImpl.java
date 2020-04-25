package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiStatistic;
import com.corgi.entity.CorgiTopic;
import com.corgi.mapper.CorgiToolMapper;
import com.corgi.mapper.CorgiUserTagMapper;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiToolService.class)
@Slf4j
@Component
public class CorgiToolServiceImpl implements CorgiToolService {
    @Autowired
    private CorgiUserTagMapper corgiUserTagMapper;

    @Autowired
    private CorgiToolMapper corgiToolMapper;

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

    @Override
    public List<CorgiTopic> searchTopic(String text, String status) {
        return corgiToolMapper.searchTopic(text, status);
    }

    @Override
    public void addTopic(CorgiTopic topic) {
        corgiToolMapper.addTopic(topic);
    }

    @Override
    public void updateTopic(CorgiTopic topic) {
        corgiToolMapper.updateTopic(topic);
    }

    @Override
    public List<String> getActivityTopic(String activityId) {
        return corgiToolMapper.getActivityTopic(activityId);
    }

    @Override
    public void updateActivityTopic(String activityId, List<String> topics) {
        corgiToolMapper.deleteActivityTopic(activityId);
        if (topics != null) {
            for (String topic : topics) {
                corgiToolMapper.addActivityTopic(activityId, topic);
            }
        }
    }

    @Override
    public void countUserNumber(String user) {
        Integer count = corgiToolMapper.getCountByUser(user);
        if (count == null) {
            corgiToolMapper.addCountByUser(user, 1);
        } else {
            corgiToolMapper.updateCountByUser(user, ++count);
        }
    }

    @Override
    public List<HashMap> getInfluencer() {
        return corgiToolMapper.getAllInfluencer();
    }
}
