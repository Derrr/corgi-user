package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.support.UserPositionSupporter;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.*;
import com.corgi.user.api.CorgiUserService;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserService.class)
@Slf4j
@Component
public class CorgiUserServiceImpl implements CorgiUserService {
    private static int MAX_PROFILE_SIZE = 16;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private CorgiUserMatchService corgiUserMatchService;

    @Override
    public UserLogin login(UserLogin userLogin) {
        userLogin.setStatus("0");
        UserLogin tmpUserLogin = corgiUserMapper.getUserLoginByTelNo(userLogin.getTelNo());
        if (tmpUserLogin != null && !StringUtils.isEmpty(tmpUserLogin.getUserId())) {
            userLogin.setUserId(tmpUserLogin.getUserId());
            if (!StringUtils.isEmpty(userLogin.getImId())) {
                corgiUserMapper.updateImId(userLogin);
            }
            userLogin.setStatus(corgiUserMapper.countUserDetail(tmpUserLogin.getUserId()) + "");
            return tmpUserLogin;
        }
        corgiUserMapper.addUserLogin(userLogin);
        return tmpUserLogin;
    }

    @Override
    public String addDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        if (corgiUserMapper.countUserDetail(userDetail.getUserId()) > 0) {
            return "user exists";
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserMapper.addUserDetail(userDetail);
        this.updatePreferGroup(userDetail.getUserId(), userDetail.getPreferGroup());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String updateDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserMapper.updateUserDetail(userDetail);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public UserDetail getUserDetail(String userId) {
        UserDetail userDetail = corgiUserMapper.getUserDetail(userId);
        if (userDetail == null) {
            return null;
        }
        List<String> groups = corgiUserMapper.getPreferGroup(userId);
        List<UserPic> userPics = corgiUserMapper.getUserPic(userId);

        userDetail.setPreferGroup(groups);
        userDetail.setUserPics(userPics);

        return userDetail;
    }

    @Override
    public String updatePreferGroup(String userId, List<String> groups) {
        if (StringUtils.isEmpty(userId)) {
            return "user id is null";
        }
        corgiUserMapper.deletePreferGroup(userId);
        if (!CollectionUtils.isEmpty(groups)) {
            for (String group : groups) {
                corgiUserMapper.addPreferGroup(userId, group);
            }
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String addUserPic(UserPic userPic) {
        corgiUserMapper.addUserPic(userPic);
        return userPic.getPicId();
    }

    @Override
    public String deleteUserPic(String picId) {
        corgiUserMapper.deleteUserPic(picId);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String updateUserPosition(UserPosition userPosition) {
        if (userPosition == null || StringUtils.isEmpty(userPosition.getUserId())) {
            return "user id is empty";
        }
        Long now = System.currentTimeMillis();
        UserPosition oldUserPosition = corgiUserMapper.getUserPosition(userPosition.getUserId());
        if (oldUserPosition == null) {
            corgiUserMapper.addUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
        } else if (!oldUserPosition.getLat().equals(userPosition.getLat()) || (!oldUserPosition.getLng().equals(userPosition.getLng()))) {
            corgiUserMapper.updateUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
        } else {
            corgiUserMapper.updateUserPositionUptime(userPosition.getUserId(), now);
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<UserProfile> getNearByUser(UserPosition userPosition, Double range) {
        UserPositionSupporter supporter = new UserPositionSupporter(userPosition, range);
        List<String> userIds = corgiUserMapper.getNearByUser(supporter);
        String inValue = getUserSql(userIds, userPosition.getUserId());
        if (StringUtils.isEmpty(inValue)) {
            return new ArrayList<>();
        }
        List<UserProfile> userProfiles = corgiUserMapper.getUserProfileList(inValue);
        UserDetail loginUserDetail = corgiUserMapper.getUserDetail(userPosition.getUserId());
        if (!CollectionUtils.isEmpty(userProfiles)) {
            for (UserProfile userProfile : userProfiles) {
                UserDetail userDetail = corgiUserMapper.getUserDetail(userProfile.getUserId());
                userProfile.setMatch(corgiUserMatchService.getUserMatchDetail(loginUserDetail, userDetail));
            }
        }
        return userProfiles;
    }

    private String getUserSql(List<String> userIds, String loginUserId) {
        //若没有人则返回空
        if (userIds == null || userIds.size() <= 1) {
            return "";
        }

        if (userIds.size() > MAX_PROFILE_SIZE * 2) {
            //若人数很多则随机取16人
            Random r = new Random();
            List<String> tmpUserIds = new ArrayList<>();
            for (int i = 0; i < MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size() - i);
                tmpUserIds.add(userIds.remove(index));
            }
            userIds = tmpUserIds;
        } else if (userIds.size() > MAX_PROFILE_SIZE) {
            //若人数不多，则剔除多余人
            Random r = new Random();
            for (int i = 0; i < userIds.size() - MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size() - i);
                userIds.remove(index);
            }

        }

        StringBuilder sb = new StringBuilder("('");
        for (String userId : userIds) {
            if (!userId.equals(loginUserId)) {
                sb.append(userId + "','");
            }
        }

        return sb.delete(sb.length() - 2, sb.length()).append(")").toString();
    }


}
