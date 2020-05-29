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
    void addActivityLike(@Param("like") ActivityLike activityLike);

    /**
     * 删除活动点赞
     * @param userId
     * @param activityId
     */
    void deleteActivityLike(@Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * 获取活动点赞
     *
     * @param activityId
     * @return
     */
    List<ActivityLike> getActivityLike(@Param("activityId") String activityId);

    /**
     * 获取点赞数
     *
     * @param activityId
     * @return
     */
    Long countActivityLike(@Param("activityId") String activityId);

    /**
     * 获取点赞数
     *
     * @param activityId
     * @param userId
     * @return
     */
    Long countUserLike(@Param("activityId") String activityId, @Param("userId")String userId);


    /**
     * 获取点赞好友
     *
     * @param userId
     * @return
     */
    List<ActivityLike> getFollowUser(@Param("userId") String userId, @Param("activityId") String activityId);

}
