package com.corgi.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeMapper {
    void initFakeUserPool(@Param("size") Integer size, @Param("time") Long time);

    void clearFakeUserPool();

    String selectFakeUser(@Param("limit") Integer limit);

    Integer countFakeUser();

    void addFakeFollower(@Param("userId") String userId, @Param("followId") String followUserId);

    Integer countFakeFollower(@Param("userId") String userId);

    Integer countFakeLike(@Param("activityId") String activityId);

    String getLastActivity(@Param("userId") String userId, @Param("time") String time);

    void updateFakeUserPosition(@Param("userId") String userId, @Param("time") Long time);
}
