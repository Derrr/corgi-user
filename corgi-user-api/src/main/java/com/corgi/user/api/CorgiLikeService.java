package com.corgi.user.api;


import com.corgi.user.entity.ActivityLike;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiLikeService {
    void addActivityLike(ActivityLike activityLike);

    List<ActivityLike> getActivityComment(String activityId);

    Long countActivityLike(String activityId);
}
