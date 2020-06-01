package com.corgi.mapper;

import com.corgi.user.entity.ActivityShare;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiShareMapper {
    /**
     * 添加活动点赞
     *
     * @param activityShare
     */
    void addActivityShare(@Param("share") ActivityShare activityShare);

    /**
     * 获取点赞数
     *
     * @param activityId
     * @return
     */
    Integer countActivityShare(@Param("activityId") String activityId);

}
