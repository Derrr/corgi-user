package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.CheckPic;
import com.corgi.mapper.CorgiBarMapper;
import com.corgi.mapper.CorgiFakeMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiFakeService.class)
@Slf4j
@Component
public class CorgiFakeServiceImpl implements CorgiFakeService {
    @Autowired
    private CorgiFakeMapper corgiFakeMapper;
    @Autowired
    private CorgiUserFollowService corgiUserFollowService;
    @Autowired
    private CorgiLikeService corgiLikeService;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiPicService corgiPicService;

    private static final List<String> PREFIX = Arrays.asList("🍌", "🍑", "🍆", "1", "0", "🐻", "💪", "🐺");
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void refreshFakeUser(Integer size) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        corgiFakeMapper.clearFakeUserPool();
        Random random = new Random();
        List<UserDetail> fakeUserDetails = corgiFakeMapper.getFakeUsers(size, calendar.getTimeInMillis());
        for (UserDetail detail : fakeUserDetails) {
            executorService.submit(() -> {
                String prefix = " ";//PREFIX.get(random.nextInt(9));
                if (detail.getNickname().startsWith("小可_")) {
                    detail.setNickname(detail.getNickname() + new Random().nextInt(10));
                } else {
                    if (random.nextInt(2) > 0) {
                        detail.setNickname(detail.getNickname() + prefix);
                    } else {
                        detail.setNickname(prefix + detail.getNickname());
                    }
                }
                String avatarStatus = "fake" + detail.getUserId();
                String fakeId = corgiFakeMapper.getFakeUserByStatus(avatarStatus);
                if (!StringUtils.isEmpty(fakeId)) {
                    corgiFakeMapper.addFakeUserPool(fakeId);
                    corgiFakeMapper.updateNickname(fakeId, detail.getNickname());
                    return;
                }
                detail.setAvatarStatus(avatarStatus);
                detail.setBackground(CheckPic.getDefaultBackground().replaceAll("https://corgi-pic\\.oss-cn-beijing\\.aliyuncs\\.com", "http://image.corgi.org.cn"));
                detail.setAvatarCheckStatus(UserDetail.NO_FACE);
                UserLogin userLogin = new UserLogin();
                userLogin.setTelNo("3" + detail.getTelNo());
                userLogin = corgiUserService.login(userLogin);
                fakeId = userLogin.getUserId();
                detail.setUserId(fakeId);
                corgiUserService.addDetail(detail);
//                if (!StringUtils.isEmpty(detail.getAvatar())) {
//                    UserPic userPic = new UserPic();
//                    userPic.setUserId(fakeId);
//                    userPic.setPicUrl(detail.getAvatar());
//                    corgiPicService.addUserPic(userPic);
//                }

                UserPosition position = new UserPosition();
                position.setUserId(fakeId);
                position.setCity(detail.getCity());
                position.setProvince(detail.getHideGroup());
                position.setVersion("0.0.0");
                position.setLat(detail.getLat() + random.nextDouble() * 2 - 1);
                position.setLng(detail.getLng() + random.nextDouble() * 2 - 1);
                position.setRealLat(position.getLat());
                position.setRealLng(position.getLng());
                corgiUserService.updateUserPosition(position);
            });

        }
        //corgiFakeMapper.initFakeUserPool(size, calendar.getTimeInMillis());
    }

    @Override
    public String selectFakeUser() {
        Integer count = corgiFakeMapper.countFakeUser();
        Random random = new Random();
        return corgiFakeMapper.selectFakeUser(random.nextInt(count));
    }

    @Override
    public Boolean addFakeFollower(String userId, String followId) {
        int result = corgiUserFollowService.isFollowed(userId, followId);
        if (result != 1 && result < 3) {
            corgiFakeMapper.addFakeFollower(userId, followId);
            corgiUserFollowService.follow(userId, followId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteFakeFollower(String userId) {
        corgiFakeMapper.deleteUserFakeFollows(userId);
    }

    @Override
    public Boolean addFakeLike(ActivityLike activityLike) {
        Integer result = corgiLikeService.countUserLike(activityLike.getActivityId(), activityLike.getLikeUserId());
        if (result > 0) {
            return false;
        }
        activityLike.setType("fake");
        corgiLikeService.addActivityLike(activityLike);
        return true;
    }

    @Override
    public Integer countFakeFollower(String userId) {
        return corgiFakeMapper.countFakeFollower(userId);
    }

    @Override
    public Integer countFakeLike(String activityId) {
        return corgiFakeMapper.countFakeLike(activityId);
    }

    @Override
    public List<CorgiActivity> getActivityByDate(String time) {
        return corgiFakeMapper.getActivityByDate(time);
    }

    @Override
    public List<CorgiActivity> getHotActivityByDate(String time, Integer threshold) {
        return corgiFakeMapper.getHotActivityByDate(time, threshold);
    }

    @Override
    public void updateFakeTime(String userId) {
        corgiFakeMapper.updateFakeUserPosition(userId, System.currentTimeMillis());
    }

    @Override
    public String getLastFakeFollowTime(String userId) {
        return corgiFakeMapper.getLastFakeFollowTime(userId);
    }

    @Override
    public String getFakeComment() {
        Integer size = corgiFakeMapper.countFakeComment();
        return corgiFakeMapper.getFakeComment(new Random().nextInt(size));
    }
}
