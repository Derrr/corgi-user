package com.corgi.user.api;


import com.corgi.user.entity.ActivityLike;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiLikeService {
    void addActivityLike(ActivityLike activityLike);

    void deleteActivityLike(String userId, String activityId);

    List<ActivityLike> getActivityLike(String activityId, Integer page, Integer pageSize);

    Long countActivityLike(String activityId);

    List<ActivityLike> getFollowUser(String userId, String activityId);

    Integer countUserLike(String activityId, String userId);

    List<String> getLikedActivity(String userId, Integer page, Integer pageSize);
}
