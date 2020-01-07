package com.corgi.user.api;

import com.corgi.entity.CorgiStatistic;
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

    List<String> getTopics();

    void addTopic(String topic);

    void deleteTopic(String topic);

    void addCount(String table, String date, Long count);

    List<CorgiStatistic> getCount(String table, String beginDate, String endDate);

    List<String> getActivityTopic(String activityId);

    void updateActivityTopic(String activityId, List<String> topics);
}
