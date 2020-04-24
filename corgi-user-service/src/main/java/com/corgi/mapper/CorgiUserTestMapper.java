package com.corgi.mapper;

import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserLogin;
import com.corgi.user.entity.UserPosition;
import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserTestMapper {
    /**
     * 通过手机获取用户登录信息
     *
     * @param telNo
     * @return
     */
    UserLogin getUserLoginByTelNo(@Param("telNo") String telNo);

    /**
     * 通过用户ID获取登录信息
     *
     * @param userId
     * @return
     */
    UserLogin getUserLogin(@Param("userId") String userId);

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
     * 更新推送信息
     *
     * @param userLogin
     */
    void updatePush(@Param("userLogin") UserLogin userLogin);

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
     *
     * @param userId
     * @param nickname
     * @param checkNickname
     */
    void updateNickname(@Param("userId") String userId, @Param("nickname") String nickname, @Param("checkNickname") String checkNickname);


    /**
     * 统计昵称
     *
     * @param nickname
     * @param checkNickname
     * @return
     */
    int countNickname(@Param("nickname") String nickname, @Param("checkNickname") String checkNickname, @Param("userId") String userId);

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
     * 统计年龄范围
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    long countBirthday(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

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
     * 删除用户董路信息
     *
     * @param userId
     */
    void deleteUserLogin(@Param("userId") String userId);

    /**
     * 删除用户详情
     *
     * @param userId
     */
    void deleteUserDetail(@Param("userId") String userId);

}
