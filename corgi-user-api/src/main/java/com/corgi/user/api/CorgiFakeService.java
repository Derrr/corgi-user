package com.corgi.user.api;


import com.corgi.user.entity.ActivityLike;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeService {
    void refreshFakeUser(Integer size);

    String selectFakeUser();

    void addFakeFollower(String userId, String followId);

    void addFakeLike(ActivityLike activityLike);

    Integer countFakeFollower(String userId);

    Integer countFakeLike(String activityId);
}
