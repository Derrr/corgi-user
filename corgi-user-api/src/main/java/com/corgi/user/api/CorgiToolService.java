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

    void addTopic(CorgiTopic topic);

    void updateTopic(CorgiTopic topic);

    List<String> getActivityTopic(String activityId);

    void updateActivityTopic(String activityId, List<String> topics);

    void countUserNumber(String user);

    List<HashMap> getInfluencer();

    void addActivityMessage(ActivityMessage activityMessage);

    List<ActivityMessage> getActivityMessage(String userId);

    Long countActivityMessage(String userId);

    void deleteActivityMessage(String userId, Long time);
}
