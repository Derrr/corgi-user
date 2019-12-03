package com.corgi.mapper.profile;

import com.corgi.user.entity.UserLogin;
import org.apache.ibatis.annotations.Param;

/**
 * @author tairanliu
 */
public interface CorgiUserMapper {
    /**
     * 通过手机获取用户登录信息
     * @param telNo
     * @return
     */
    UserLogin getUserLoginByTelNo(@Param("telNo") String telNo);

    /**
     * 更新用户推送ID
     * @param userLogin
     */
    void updateImId(@Param("userLogin")UserLogin userLogin);

    /**
     * 添加用户登录信息
     * @param userLogin
     */
    void addUserLogin(@Param("userLogin") UserLogin userLogin);
}
