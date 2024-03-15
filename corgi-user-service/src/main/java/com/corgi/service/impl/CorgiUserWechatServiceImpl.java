package com.corgi.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
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
    public void updateUserWechat(UserWechat userWechat) {
        UserWechat wechat = corgiUserWechatMapper.getUserWechat(userWechat.getUserId());
        if (wechat == null) {
            if (StringUtils.isEmpty(userWechat.getStatus())) {
                userWechat.setStatus("1");
            }
            corgiUserWechatMapper.addUserWechat(userWechat);
            return;
        }
        if (StringUtils.isEmpty(userWechat.getStatus())) {
            userWechat.setStatus("1");
        }
        corgiUserWechatMapper.updateUserWechat(userWechat);
    }

    @Override
    public UserWechat getUserWechat(String userId) {
        return corgiUserWechatMapper.getUserWechat(userId);
    }

    @Override
    public List<UserWechat> listUserPaidWechats(String userId, Integer page, Integer pageSize) {
        return corgiUserWechatMapper.listPaidWechats(userId, (page - 1) * pageSize, pageSize);
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
