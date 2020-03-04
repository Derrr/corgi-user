package com.corgi.mapper;

import com.corgi.user.entity.UserMatch;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserActivityMapper {
    /**
     * 添加报名
     *
     * @param userSignUp
     */
    void addSignUp(@Param("userSignUp") UserSignUp userSignUp);


    /**
     * 取消报名
     *
     * @param userSignUp
     */
    void deleteSignUp(@Param("userSignUp") UserSignUp userSignUp);

    /**
     * 删除活动下报名
     *
     * @param activityId
     */
    void deleteSignUpByActivity(@Param("activityId") String activityId);

    /**
     * 修改报名人状态
     *
     * @param userSignUp
     */
    void updateSignUp(@Param("userSignUp") UserSignUp userSignUp);

    /**
     * 获取用户报名状态
     *
     * @param userId
     * @param activityId
     * @return
     */
    Integer getStatus(@Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * 获取所有报名人
     *
     * @param activityId
     * @return
     */
    List<UserProfile> getUser(@Param("activityId") String activityId, String status);

    /**
     * 获取报名活动
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<String> getSignUpActivityId(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);
}
