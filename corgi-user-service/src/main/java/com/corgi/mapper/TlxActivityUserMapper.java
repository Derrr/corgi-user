package com.corgi.mapper;

import com.corgi.user.entity.TlxActivity;
import com.corgi.user.entity.UserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface TlxActivityUserMapper {

    /**
     * 参与活动
     *
     * @param activityId
     * @return
     */
    void addActivityUser(@Param("activityId") String activityId, @Param("userId")String userId);

    /**
     * 取消活动
     *
     * @param activityId
     * @return
     */
    void deleteActivityUser(@Param("activityId") String activityId, @Param("userId")String userId);

    /**
     * 获取活动人员
     * @param activityId
     */
    List<UserDetail> getActivityUsers(@Param("activityId") String activityId);

    /**
     * 统计活动人员
     * @param activityId
     */
    Integer countActivityUser(@Param("activityId") String activityId);

}
