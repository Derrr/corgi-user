package com.corgi.mapper;

import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserMapper {
    /**
     * 通过手机获取用户登录信息
     *
     * @param telNo
     * @return
     */
    UserLogin getUserLoginByTelNo(@Param("telNo") String telNo);

    /**
     * 通过用户ID获取登录信息
     * @param userId
     * @return
     */
    UserLogin getUserLogin(@Param("userId")String userId);

    /**
     * 更新用户推送ID
     *
     * @param userLogin
     */
    void updateImId(@Param("userLogin") UserLogin userLogin);

    /**
     * 更新用户推送ID
     *
     * @param userLogin
     */
    void updateLogin(@Param("userLogin") UserLogin userLogin);


    /**
     * 注册用户登录信息
     *
     * @param userLogin
     */
    void addUserLogin(@Param("userLogin") UserLogin userLogin);

    /**
     * 查询是否有对应用户资料
     *
     * @param userId
     * @return
     */
    int countUserDetail(@Param("userId") String userId);

    /**
     * 添加用户资料
     *
     * @param userDetail
     */
    void addUserDetail(@Param("userDetail") UserDetail userDetail);

    /**
     * 修改用户资料
     *
     * @param userDetail
     */
    void updateUserDetail(@Param("userDetail") UserDetail userDetail);

    /**
     * 更新昵称
     * @param userId
     * @param nickname
     */
    void updateNickname(@Param("userId") String userId,@Param("nickname")String nickname);


    /**
     * 统计昵称
     * @param nickname
     * @return
     */
    int countNickname(@Param("nickname")String nickname);

    /**
     * 修改头像状态
     *
     * @param dataId
     * @param status
     */
    void updateUserAvatar(@Param("dataId") String dataId, @Param("status") String status);

    /**
     * 删除头像
     *
     * @param dataId
     */
    void deleteUserAvatar(@Param("dataId") String dataId);

    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    UserDetail getUserDetail(@Param("userId") String userId);

    /**
     * 删除喜欢类型
     *
     * @param userId
     */
    void deletePreferGroup(@Param("userId") String userId);

    /**
     * 添加喜欢类型
     *
     * @param userId
     * @param group
     */
    void addPreferGroup(@Param("userId") String userId, @Param("group") String group);

    /**
     * 获取喜欢类型
     *
     * @param userId
     * @return
     */
    List<String> getPreferGroup(@Param("userId") String userId);

    /**
     * 添加用户位置
     *
     * @param userId
     * @param lat
     * @param lng
     * @param uptime
     */
    void addUserPosition(@Param("userId") String userId, @Param("lat") Double lat, @Param("lng") Double lng, @Param("uptime") Long uptime);

    /**
     * 获取用户位置
     *
     * @param userId
     * @return
     */
    UserPosition getUserPosition(@Param("userId") String userId);

    /**
     * 更新用户位置
     *
     * @param userId
     * @param lat
     * @param lng
     * @param uptime
     */
    void updateUserPosition(@Param("userId") String userId, @Param("lat") Double lat, @Param("lng") Double lng, @Param("uptime") Long uptime);

    /**
     * 更新用户时间
     *
     * @param userId
     * @param uptime
     */
    void updateUserPositionUptime(@Param("userId") String userId, @Param("uptime") Long uptime);

    /**
     * 获取周围人信息
     *
     * @param supporter
     * @return
     */
    List<String> getNearByUser(@Param("range") UserQuerySupporter supporter);

    /**
     * 获取人员信息列表
     *
     * @param inValue
     * @return
     */
    List<UserProfile> getUserProfileList(@Param("inValue") String inValue);

    /**
     * 查询用户
     *
     * @param userDetail
     * @param size
     * @param start
     * @return
     */
    List<UserProfile> queryUserProfile(@Param("userDetail") UserDetail userDetail, @Param("start") int start, @Param("size") int size);

    /**
     * 统计用户
     *
     * @param userDetail
     * @return
     */
    long countUserProfile(@Param("userDetail") UserDetail userDetail);


    /**
     * 获取活跃用户数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    long countActiveUser(@Param("beginTime") long beginTime, @Param("endTime") long endTime);

    /**
     * 获取注册用户数
     *
     * @param date
     * @return
     */
    long countRegisterUser(@Param("date") String date);

    /**
     * 过滤用户
     *
     * @param userId
     * @param role
     * @param group
     * @param preferGroup
     * @return
     */
    List<String> filterUser(@Param("userId") String userId, @Param("role") String role, @Param("group") String group, @Param("preferGroup") String preferGroup);
}
