package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserWechatMapper;
import com.corgi.user.api.CorgiUserWechatService;
import com.corgi.user.entity.UserWechat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserWechatService.class)
@Slf4j
@Component
public class CorgiUserWechatServiceImpl implements CorgiUserWechatService {

    @Autowired
    private CorgiUserWechatMapper corgiUserWechatMapper;

    @Override
    public String addUserWechat(UserWechat userWechat) {
        corgiUserWechatMapper.addUserWechat(userWechat);
        return userWechat.getId();
    }

    @Override
    public void updateUserWechat(UserWechat userWechat) {
        corgiUserWechatMapper.updateUserWechat(userWechat);
    }

    @Override
    public UserWechat getUserWechat(String userId) {
        return corgiUserWechatMapper.getUserWechat(userId);
    }

    @Override
    public List<UserWechat> listUserPaidWechats(String userId) {
        return corgiUserWechatMapper.listPaidWechats(userId);
    }

    @Override
    public List<UserWechat> queryWechat(UserWechat userWechat, Integer page, Integer pageSize) {
        return corgiUserWechatMapper.queryWechat(userWechat, (page - 1) * pageSize, pageSize);
    }

    @Override
    public Integer countWechat(UserWechat userWechat) {
        return corgiUserWechatMapper.countWechat();
    }
}
