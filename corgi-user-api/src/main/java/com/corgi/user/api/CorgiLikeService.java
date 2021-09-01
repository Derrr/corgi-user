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

    List<String> getLikedActivity(String userId, String activityId, String category, Integer page, Integer pageSize);

    long countLikeByDate(String date, String category);

    long countLikeUserByDate(String date, String category);

    Integer countUserLikeByDate(String userId, String date);

    List<ActivityLike> getLikeByPage(Integer page, Integer pageSize);

    List<ActivityLike> queryLike(ActivityLike query, Integer size);

    Double getAvgLike(String userId);
}
