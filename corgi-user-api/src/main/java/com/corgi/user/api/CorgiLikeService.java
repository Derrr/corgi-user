package com.corgi.user.api;


import com.corgi.user.entity.ActivityLike;
import com.corgi.user.entity.UserProfile;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiLikeService {
    Integer addActivityLike(ActivityLike activityLike);

    Integer deleteActivityLike(String userId, String activityId);

    List<ActivityLike> getActivityLike(String activityId, Integer page, Integer pageSize);

    Long countActivityLike(String activityId);

    Integer countRealActivityLike(String activityId);

    List<ActivityLike> getFollowUser(String userId, String activityId);

    Integer countUserLike(String activityId, String userId);

    List<String> getLikedActivity(String userId, Integer page, Integer pageSize);

    long countLikeByDate(String date, String category);

    long countLikeUserByDate(String date, String category);

    List<ActivityLike> getLikeByPage(Integer page, Integer pageSize);

    Double getAvgLike(String userId);
}
