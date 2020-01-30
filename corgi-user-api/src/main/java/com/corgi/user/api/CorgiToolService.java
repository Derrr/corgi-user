package com.corgi.user.api;

import com.corgi.entity.CorgiStatistic;
import com.corgi.entity.CorgiTopic;
import com.corgi.user.entity.*;

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

    List<CorgiTopic> getTopics(String status);

    List<CorgiTopic> searchTopic(String text);

    void addTopic(CorgiTopic topic);

    void updateTopic(CorgiTopic topic);

    void addCount(String table, String date, Long count);

    List<CorgiStatistic> getCount(String table, String beginDate, String endDate);

    long sumCount(String table);

    List<String> getActivityTopic(String activityId);

    void updateActivityTopic(String activityId, List<String> topics);
}
