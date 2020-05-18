package com.corgi.mapper;

import com.corgi.user.entity.ActivityLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiLikeMapper {
    /**
     * 添加活动评论
     *
     * @param activityLike
     */
    void addActivityLike(@Param("like") ActivityLike activityLike);

    /**
     * 获取活动评论
     * @param activityId
     * @return
     */
    List<ActivityLike> getActivityLike(@Param("activityId") String activityId);

    /**
     * 获取评论数
     * @param activityId
     * @return
     */
    Long countActivityLike(@Param("activityId") String activityId);

}
