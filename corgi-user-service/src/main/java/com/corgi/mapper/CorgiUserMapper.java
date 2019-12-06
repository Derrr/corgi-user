package com.corgi.mapper;

import com.corgi.user.entity.UserLogin;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserPic;
import com.corgi.user.entity.UserPosition;
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
     * 更新用户推送ID
     *
     * @param userLogin
     */
    void updateImId(@Param("userLogin") UserLogin userLogin);

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
     * 添加用户图片
     *
     * @param userPic
     */
    void addUserPic(@Param("userPic") UserPic userPic);

    /**
     * 删除用户图片
     *
     * @param picId
     */
    void deleteUserPic(@Param("picId") String picId);

    /**
     * 获取用户图片
     *
     * @param userId
     * @return
     */
    List<UserPic> getUserPic(@Param("userId") String userId);

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

}
