package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserWechatService {
    void updateUserWechat(UserWechat userWechat);

    void updateUserWechatCount(String userId, Integer total, Integer period, Integer weight);

    UserWechat getUserWechat(String userId);

    List<UserWechat> listUserPaidWechats(String userId, Integer page, Integer pageSize);

    List<UserWechat> queryWechat(UserWechat userWechat, Integer page, Integer pageSize);

    Integer countWechat(UserWechat userWechat);
}
