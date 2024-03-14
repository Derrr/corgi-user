package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserWechatService {
    String addUserWechat(UserWechat userWechat);

    void updateUserWechat(UserWechat userWechat);

    UserWechat getUserWechat(String userId);

    List<UserWechat> listUserPaidWechats(String userId);

    List<UserWechat> queryWechat(UserWechat userWechat, Integer page, Integer pageSize);

    Integer countWechat(UserWechat userWechat);
}
