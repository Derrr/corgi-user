package com.corgi.user.api;

import com.corgi.entity.CorgiTopic;
import com.corgi.user.entity.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiToolService {
    List<String> getTags();

    List<String> getUserTags(String userId);

    List<String> getInterestsByCategory(String category);

    List<UserInterest> getUserInterest(String userId);

    void updateUserTag(String userId, List<String> tags);

    void updateUserInterest(String userId, String category, List<String> interests);

    List<CorgiTopic> searchTopic(String text, String status);

    void deleteActivityMessageByMessage(ActivityMessage message);

    void addTopic(CorgiTopic topic);

    void updateTopic(CorgiTopic topic);

    List<String> getActivityTopic(String activityId);

    void updateActivityTopic(String activityId, List<String> topics);

    void countUserNumber(String user);

    List<HashMap> getInfluencer();

    Integer getCountByUser(String userId);

    void addActivityMessage(ActivityMessage activityMessage);

    List<ActivityMessage> getActivityMessage(String userId, Integer pageSize);

    List<ActivityMessage> getAllActivityMessage(String userId, Integer page, Integer pageSize);

    Long countActivityMessage(String userId);

    ActivityMessage getLastActivityMessage(String userId);

    List<ActivityMessage> getActivityMessageByType(String userId, Integer pageSize, String type);

    List<ActivityMessage> getAllActivityMessageByType(String userId, Integer page, Integer pageSize, String type);

    Long countActivityMessageByType(String userId, String type);

    ActivityMessage getLastActivityMessageByType(String userId, String type);

    void deleteActivityMessage(String userId, Long time);

    void bindWechat(String wechatId, String corgId);

    String getIdByWechatId(String wechatId);
}
