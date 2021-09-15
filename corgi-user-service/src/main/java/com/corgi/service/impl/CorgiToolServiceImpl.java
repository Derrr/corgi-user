package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.CorgiActivity;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Reference
    private CorgiActivityService activityService;

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
        if (tags != null) {
            for (String tag : tags) {
                corgiUserTagMapper.addUserTag(userId, tag);
            }
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
    public CorgiTopic getTopic(String id) {
        return corgiToolMapper.getTopicById(id);
    }

    @Override
    public List<String> getActivityTypes() {
        return corgiToolMapper.getActivityTypes();
    }

    @Override
    public List<DateType> getDateTypes() {
        return corgiToolMapper.getDateTypes();
    }

    @Override
    public void deleteActivityMessageByMessage(ActivityMessage message) {
        corgiToolMapper.deleteActivityMessageByMessage(message);
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
    public List<CorgiTopic> getActivityTopicDetails(String activityId) {
        return corgiToolMapper.getActivityTopicDetails(activityId);
    }

    @Override
    public List<String> getActivityIdsByTopic(String topic, Integer page, Integer size) {
        return corgiToolMapper.getActivityIdsByTopic(topic, (page - 1) * size, size);
    }

    @Override
    public void updateActivityTopicWeight(String activityId, Integer weight) {
        corgiToolMapper.updateActivityWeight(activityId, weight);
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

    @Override
    public Integer getCountByUser(String userId) {
        return corgiToolMapper.getCountByUser(userId);
    }


    @Override
    public void addActivityMessage(ActivityMessage activityMessage) {
        if (corgiToolMapper.countActivityMessageByMessage(activityMessage) > 0) {
            return;
        }
        corgiToolMapper.addActivityMessage(activityMessage);
    }

    @Override
    public List<ActivityMessage> getActivityMessage(String userId, Integer pageSize) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getActivityMessage(userId, pageSize);
        corgiToolMapper.readActivityMessage(userId);
        return buildActivityMessage(activityMessages);
    }

    @Override
    public List<ActivityMessage> getAllActivityMessage(String userId, Integer page, Integer pageSize) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getAllActivityMessage(userId, (page - 1) * pageSize, pageSize);
        corgiToolMapper.readActivityMessage(userId);
        return buildActivityMessage(activityMessages);
    }

    @Override
    public Long countActivityMessage(String userId) {
        if (StringUtils.isEmpty(userId)) {
            userId = "8";
        }
        return corgiToolMapper.countActivityMessage(userId);
    }

    @Override
    public ActivityMessage getLastActivityMessage(String userId) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getActivityMessage(userId, 1);
        if (CollectionUtils.isEmpty(activityMessages)) {
            return null;
        }
        return activityMessages.get(0);
    }

    @Override
    public List<ActivityMessage> getActivityMessageByType(String userId, Integer pageSize, String type) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getActivityMessageByType(userId, pageSize, type);
        corgiToolMapper.readActivityMessageByType(userId, type);
        return buildActivityMessage(activityMessages);
    }

    @Override
    public List<ActivityMessage> getAllActivityMessageByType(String userId, Integer page, Integer pageSize, String type) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getAllActivityMessageByType(userId, type, (page - 1) * pageSize, pageSize);
        corgiToolMapper.readActivityMessageByType(userId, type);
        return buildActivityMessage(activityMessages);
    }

    @Override
    public Long countActivityMessageByType(String userId, String type) {
        ActivityMessage activityMessage = new ActivityMessage();
        activityMessage.setToUserId(userId);
        activityMessage.setMessageType(type);
        activityMessage.setIsRead("0");
        return Long.valueOf(corgiToolMapper.countActivityMessageByMessage(activityMessage));
    }

    @Override
    public ActivityMessage getLastActivityMessageByType(String userId, String type) {
        List<ActivityMessage> activityMessages = corgiToolMapper.getActivityMessageByType(userId, 1, type);
        if (CollectionUtils.isEmpty(activityMessages)) {
            return null;
        }
        return activityMessages.get(0);
    }

    @Override
    public void deleteActivityMessage(String userId, Long time) {
        if (StringUtils.isEmpty(userId)) {
            userId = "8";
        }
        corgiToolMapper.deleteActivityMessage(userId, time);
    }

    @Override
    public void bindWechat(String wechatId, String corgiId) {
        corgiToolMapper.bindWechatId(wechatId, corgiId);
    }

    @Override
    public String getIdByWechatId(String wechatId) {
        return corgiToolMapper.getIdByWechat(wechatId);
    }

    public List<ActivityMessage> buildActivityMessage(List<ActivityMessage> activityMessages) {
        if (CollectionUtils.isEmpty(activityMessages)) {
            return new ArrayList<>();
        }
        for (ActivityMessage activityMessage : activityMessages) {
            String activityId = activityMessage.getActivityId();
            List<CorgiActivity> activities = activityService.getActivityByIds(Arrays.asList(activityId));
            if (CollectionUtils.isEmpty(activities)) {
                activityMessage.setStatus(CorgiActivity.DELETED);
                continue;
            }
            CorgiActivity activity = activities.get(0);

            if (!StringUtils.isEmpty(activity.getCoverUrl())) {
                activityMessage.setActivityPic(activity.getCoverUrl());
            } else {
                if (CollectionUtils.isEmpty(activities.get(0).getPics())) {
                    continue;
                }
                activityMessage.setActivityPic(activity.getPics().get(0).getPicUrl());
            }
            String content = activity.getContent();
            if (!StringUtils.isEmpty(content)) {
                activityMessage.setText(content);
            } else {
                activityMessage.setText("");
            }
            activityMessage.setStatus(CorgiActivity.DELETED.equals(activity.getStatus()) ? CorgiActivity.DELETED : CorgiActivity.CREATED);
        }
        return activityMessages;
    }
}
