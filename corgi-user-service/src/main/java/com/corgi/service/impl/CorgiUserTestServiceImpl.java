package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.common.CorgiQueueName;
import com.corgi.common.messages.MatchRefresher;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.*;
import com.corgi.support.UserQuerySupporter;
import com.corgi.user.api.CorgiUserTestService;
import com.corgi.user.entity.*;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserTestService.class)
@Slf4j
@Component
public class CorgiUserTestServiceImpl implements CorgiUserTestService {
    @Autowired
    private CorgiUserTestMapper corgiUserTestMapper;

    @Override
    public UserLogin login(UserLogin userLogin) {
        userLogin.setStatus("-1");
        UserLogin tmpUserLogin = corgiUserTestMapper.getUserLoginByTelNo(userLogin.getTelNo());
        if (tmpUserLogin != null && !StringUtils.isEmpty(tmpUserLogin.getUserId())) {
            userLogin.setUserId(tmpUserLogin.getUserId());
            if (!StringUtils.isEmpty(userLogin.getImId())) {
                corgiUserTestMapper.updateImId(userLogin);
            }
            userLogin.setStatus(String.valueOf(corgiUserTestMapper.countUserDetail(tmpUserLogin.getUserId())));
            return userLogin;
        }
        corgiUserTestMapper.addUserLogin(userLogin);
        return userLogin;
    }

    @Override
    public UserLogin getUserLogin(String userId) {
        return corgiUserTestMapper.getUserLogin(userId);
    }

    @Override
    public String addDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        if (corgiUserTestMapper.countUserDetail(userDetail.getUserId()) > 0) {
            return CorgiConstants.SUCCESS;
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserTestMapper.addUserDetail(userDetail);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String updateDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserTestMapper.updateUserDetail(userDetail);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public UserDetail getUserDetail(String userId, String loginUserId) {
        UserDetail userDetail = corgiUserTestMapper.getUserDetail(userId);
        if (userDetail == null) {
            return null;
        }
        return userDetail;
    }

    @Override
    public long countBirthday(String beginDate, String endDate) {
        return corgiUserTestMapper.countBirthday(beginDate, endDate);
    }

    @Override
    public List<UserProfile> searchUsers(UserDetail userDetail, String userId, Integer page, Integer pageSize) {
        List<UserProfile> userProfiles = corgiUserTestMapper.queryUserProfile(userDetail, page < 1 ? 0 : (page - 1) * pageSize, pageSize);
        return userProfiles;
    }

    @Override
    public long countUsers(UserDetail userDetail) {
        return corgiUserTestMapper.countUserProfile(userDetail);
    }

    @Override
    public String updateUserLogin(UserLogin userLogin) {
        corgiUserTestMapper.updateLogin(userLogin);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public void updatePush(UserLogin userLogin) {
        corgiUserTestMapper.updatePush(userLogin);
    }

    @Override
    public String updateUserNickname(String userId, String nickname, String checkNickname) {
        UserDetail detail = corgiUserTestMapper.getUserDetail(userId);
        if ("fail".equals(detail.getCheckStatus())) {
            corgiUserTestMapper.updateNickname(userId, nickname, detail.getCheckNickname());
        } else {
            int count = corgiUserTestMapper.countNickname(nickname, checkNickname, userId);
            if (count > 0) {
                return "nickname exists";
            }
            corgiUserTestMapper.updateNickname(userId, nickname, checkNickname);
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public int countUserNickname(String nickname) {
        return corgiUserTestMapper.countNickname(nickname, "", "");
    }

    @Override
    public void deleteUser(String userId) {
        corgiUserTestMapper.deleteUserLogin(userId);
        corgiUserTestMapper.deleteUserDetail(userId);
    }

}
