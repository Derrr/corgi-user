package com.corgi.mapper;

import com.corgi.activity.entity.CorgiActivity;
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
    List<UserProfile> getUser(@Param("activityId") String activityId, @Param("status") String status);

    /**
     * 获取最热门报名人
     *
     * @param activityId
     * @param status
     * @return
     */
    List<UserProfile> getPopularUser(@Param("activityId") String activityId, @Param("status") String status);


    /**
     * 统计报名人
     *
     * @param activityId
     * @param status
     * @return
     */
    Integer countUser(@Param("activityId") String activityId, @Param("status") String status);


    /**
     * 获取报名活动
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<String> getSignUpActivityId(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 添加
     *
     * @param activityId
     * @param creatorId
     * @param category
     */
    void addActivityCreator(@Param("activityId") String activityId, @Param("creatorId") String creatorId, @Param("category") String category);

    /**
     * 失效
     *
     * @param activityId
     */
    void failActivityCreator(@Param("activityId") String activityId);

    /**
     * 删除
     *
     * @param activityId
     */
    void deleteActivityCreator(@Param("activityId") String activityId);

    /**
     * 按热度获取活动ID
     *
     * @param category
     * @param date
     * @param start
     * @param size
     * @return
     */
    List<String> getHeadActivityPic(@Param("category") String category, @Param("date") String date, @Param("start") Integer start, @Param("size") Integer size, @Param("topic")String topic);

    /**
     * 统计活动数
     *
     * @param category
     * @param date
     * @return
     */
    long countActivity(@Param("category") String category, @Param("date") String date);

    /**
     * 统计发活动人数
     *
     * @param category
     * @param date
     * @return
     */
    long countActivityUser(@Param("category") String category, @Param("date") String date);

    /**
     * 统计报名人数
     *
     * @param date
     * @return
     */
    long countSignUpUser(@Param("date") String date);


    /**
     * 获取参与活动
     *
     * @param userId
     * @return
     */
    List<String> getParticipateActivity(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);
}
