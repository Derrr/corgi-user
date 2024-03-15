package com.corgi.mapper;

import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserWechatMapper {
    UserWechat getUserWechat(@Param("userId")String userId);

    void updateUserWechat(@Param("wechat")UserWechat userWechat);

    void addUserWechat(@Param("wechat")UserWechat userWechat);

    List<UserWechat> listPaidWechats(@Param("userId")String userId,@Param("start")Integer start, @Param("size")Integer size);

    List<UserWechat> queryWechat(@Param("wechat")UserWechat userWechat,@Param("start")Integer start, @Param("size")Integer size);

    Integer countWechat();
}
