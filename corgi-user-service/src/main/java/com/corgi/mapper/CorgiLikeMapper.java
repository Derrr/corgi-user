package com.corgi.mapper;

import com.corgi.user.entity.ActivityLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiLikeMapper {
    /**
     * 添加活动点赞
     *
     * @param activityLike
     */
    Integer addActivityLike(@Param("like") ActivityLike activityLike);

    /**
     * 删除活动点赞
     *
     * @param userId
     * @param activityId
     */
    Integer deleteActivityLike(@Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * 获取活动点赞
     *
     * @param activityId
     * @return
     */
    List<ActivityLike> getActivityLike(@Param("activityId") String activityId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 获取点赞数
     *
     * @param activityId
     * @return
     */
    Long countActivityLike(@Param("activityId") String activityId);

    /**
     * 真实点赞数
     *
     * @param activityId
     * @return
     */
    Integer countRealActivityLike(@Param("activityId") String activityId);


    /**
     * 获取点赞数
     *
     * @param activityId
     * @param userId
     * @return
     */
    Integer countUserLike(@Param("activityId") String activityId, @Param("userId") String userId);


    /**
     * 获取点赞好友
     *
     * @param userId
     * @return
     */
    List<ActivityLike> getFollowUser(@Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * 获取点赞活动
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<String> getLikedActivityId(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 统计点赞数
     *
     * @param date
     * @param category
     * @return
     */
    long countLikeByDate(@Param("date") String date, @Param("category") String category);

    /**
     * 统计点赞人数
     *
     * @param date
     * @param category
     * @return
     */
    long countLikeUserByDate(@Param("date") String date, @Param("category") String category);

    /**
     * 全量获取真实点赞
     * @param start
     * @param size
     * @return
     */
    List<ActivityLike> getLikeByPage(@Param("start") Integer start, @Param("size") Integer size);

}
