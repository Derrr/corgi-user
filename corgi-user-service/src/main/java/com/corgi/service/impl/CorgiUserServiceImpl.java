package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.profile.CorgiUserMapper;
import com.corgi.user.entity.UserLogin;
import com.corgi.user.api.CorgiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserService.class)
@Slf4j
@Component
public class CorgiUserServiceImpl implements CorgiUserService {
    @Autowired
    private CorgiUserMapper corgiUserMapper;

    @Override
    public String login(UserLogin userLogin) {
        UserLogin tmpUserLogin = corgiUserMapper.getUserLoginByTelNo(userLogin.getTelNo());
        if (tmpUserLogin != null && !StringUtils.isEmpty(tmpUserLogin.getUserId())) {
            if (!StringUtils.isEmpty(tmpUserLogin.getImId())) {
                corgiUserMapper.updateImId(tmpUserLogin);
            }
            return tmpUserLogin.getUserId();
        }
        corgiUserMapper.addUserLogin(userLogin);
        return userLogin.getUserId();
    }
}
