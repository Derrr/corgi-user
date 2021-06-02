package com.corgi.mapper;

import com.corgi.activity.entity.CorgiActivity;
import com.corgi.user.entity.UserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeMapper {
    void initFakeUserPool(@Param("size") Integer size, @Param("time") Long time);

    void addFakeUserPool(@Param("userId") String userId);

    List<UserDetail> getFakeUsers(@Param("size") Integer size, @Param("time") Long time);

    String getFakeUserByStatus(@Param("status") String status);

    void updateFakeUserByStatus(@Param("status") String status, @Param("nickname") String nickname);

    void clearFakeUserPool();

    void deleteUserFakeFollows(@Param("userId")String userId);

    String selectFakeUser(@Param("limit") Integer limit);

    Integer countFakeUser();

    void addFakeFollower(@Param("userId") String userId, @Param("followId") String followUserId);

    Integer countFakeFollower(@Param("userId") String userId);

    Integer countFakeLike(@Param("activityId") String activityId);

    List<CorgiActivity> getActivityByDate(@Param("time") String time);

    void updateFakeUserPosition(@Param("userId") String userId, @Param("time") Long time);

    String getLastFakeFollowTime(@Param("userId") String userId);

    void updateNickname(@Param("userId")String userId, @Param("nickname")String nickname);
}
