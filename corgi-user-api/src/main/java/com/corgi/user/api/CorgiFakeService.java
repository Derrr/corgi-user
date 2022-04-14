package com.corgi.user.api;


import com.corgi.activity.entity.CorgiActivity;
import com.corgi.user.entity.ActivityLike;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeService {
    void refreshFakeUser(Integer size);

    String selectFakeUser();

    Boolean addFakeFollower(String userId, String followId);

    void deleteFakeFollower(String userId);

    Boolean addFakeLike(ActivityLike activityLike);

    Integer countFakeFollower(String userId);

    Integer countFakeLike(String activityId);

    List<CorgiActivity> getActivityByDate(String time);

    List<CorgiActivity> getHotActivityByDate(String time,Integer threshold);

    void updateFakeTime(String userId);

    String getLastFakeFollowTime(String userId);

    String getFakeComment();
}
