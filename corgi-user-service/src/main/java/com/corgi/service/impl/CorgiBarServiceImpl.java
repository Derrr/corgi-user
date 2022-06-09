package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.common.CorgiConstants;
import com.corgi.common.CorgiQueueName;
import com.corgi.common.messages.MatchRefresher;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.*;
import com.corgi.support.UserQuerySupporter;
import com.corgi.user.api.*;
import com.corgi.user.entity.*;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiBarService.class)
@Slf4j
@Component
public class CorgiBarServiceImpl implements CorgiBarService {
    @Autowired
    private CorgiBarMapper corgiBarMapper;
    @Autowired
    private CorgiUserFollowService corgiUserFollowService;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private CorgiVideoService corgiVideoService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Reference
    private CorgiActivityService corgiActivityService;


    @Override
    public List<BarProfile> getBarList(String status) {
        List<BarProfile> barProfiles = corgiBarMapper.getBarList(status);
        if (!CollectionUtils.isEmpty(barProfiles)) {
            for (BarProfile barProfile : barProfiles) {
                barProfile.setHeat(countBarHeat(barProfile));
            }
        }
        return barProfiles;
    }

    @Override
    public List<BarProfile> getBarAccountList(String status) {
        List<BarProfile> barProfiles = corgiBarMapper.getBarAccountList(status);
        return barProfiles;
    }

    @Override
    public List<BarProfile> getBarListByCity(String city, Double lat, Double lng) {
        if ((lat != null && lat > 200) || (lng != null && lng > 200)) {
            lat = null;
            lng = null;
        }
        return corgiBarMapper.getBarListByCity(city, lat, lng);
    }

    @Override
    public List<String> getBarActivity(String city, Integer size) {
        return corgiBarMapper.getBarActivity(city, size);
    }

    @Override
    public BarProfile getBarProfile(String barId) {
        BarProfile barProfile = corgiBarMapper.getBar(barId);
        if (barProfile != null) {
            CorgiActivity corgiActivity = new CorgiActivity();
            corgiActivity.setUserId(barId);
            corgiActivity.setStatus(CorgiActivity.NOT_DELETED);
            corgiActivity.setCategory(CorgiActivity.CAT_BUSINESS);
            List<UserVideo> userVideos = corgiVideoService.getVideo(barId);
            barProfile.setHeat(countBarHeat(barProfile));
            String key = "bar_count_" + barId;
            String barCountStr = redisTemplate.opsForValue().get(key);
            Long barCount;
            if (StringUtils.isEmpty(barCountStr)) {
                barCount = corgiActivityService.countBarAppraisedActivity(barId);
                redisTemplate.opsForValue().set(key, barCount + "", 1l, TimeUnit.HOURS);
            } else {
                barCount = Long.valueOf(barCountStr);
            }
            barProfile.setRelActivityCount(barCount);
            barProfile.setActivityCount((int) corgiActivityService.countCorgiActivity(corgiActivity));
            if (!CollectionUtils.isEmpty(userVideos)) {
                barProfile.setVideo(userVideos.get(0).getVideoUrl());
            }
        }

        return barProfile;
    }

    @Override
    public void updateBarProfile(BarProfile barProfile) {
        if (!StringUtils.isEmpty(barProfile.getBarId())) {
            corgiBarMapper.updateBar(barProfile);
        }
    }

    @Override
    public void addBarProfile(BarProfile barProfile) {
        String prefix = "B";
        if (!StringUtils.isEmpty(barProfile.getCuid())) {
            prefix = "C";
            barProfile.setStatus(BarProfile.STATUS_CHECKING);
        }
        String maxBarId = corgiBarMapper.getMaxBarId(prefix);
        barProfile.setBarId(createBarId(maxBarId, prefix));
        if (StringUtils.isEmpty(barProfile.getStatus())) {
            barProfile.setStatus(BarProfile.STATUS_ENABLE);
        }
        corgiBarMapper.addBar(barProfile);
        if (!CollectionUtils.isEmpty(barProfile.getBarPics())) {
            for (BarPic pic : barProfile.getBarPics()) {
                UserPic userPic = new UserPic();
                userPic.setUserId(barProfile.getBarId());
                userPic.setPicUrl(pic.getPicUrl());
                corgiPicService.addUserPic(userPic);
            }
        }


    }

    @Override
    public List<BarProfile> searchBar(BarProfile barProfile) {
        if (barProfile.getLat() != null && barProfile.getLat() > 200) {
            barProfile.setLat(null);
        }
        if (barProfile.getLng() != null && barProfile.getLng() > 200) {
            barProfile.setLng(null);
        }
        return corgiBarMapper.searchBar(barProfile);
    }

    @Override
    public void setBarAccount(String barId, String account, String password) {
        corgiBarMapper.setBarAccount(barId, account, password);
    }

    @Override
    public BarProfile getBarByAccount(String account, String password) {
        BarProfile barProfile = corgiBarMapper.getBarByAccount(account, password);
        CorgiActivity corgiActivity = new CorgiActivity();
        corgiActivity.setUserId(barProfile.getBarId());
        corgiActivity.setStatus(CorgiActivity.NOT_DELETED);
        barProfile.setActivityCount((int) corgiActivityService.countCorgiActivity(corgiActivity));
        List<UserVideo> userVideos = corgiVideoService.getVideo(barProfile.getBarId());
        if (!CollectionUtils.isEmpty(userVideos)) {
            barProfile.setVideo(userVideos.get(0).getVideoUrl());
        }
        return barProfile;
    }

    @Override
    public List<String> getBarAroundActivity(String barId, Integer page, Integer pageSize) {
        BarProfile profile = corgiBarMapper.getBar(barId);
        return corgiBarMapper.getBarActivityByRange(barId, profile.getLat(), profile.getLng(), (page - 1) * pageSize, pageSize);
    }

    private String createBarId(String maxBarId, String prefix) {
        if (StringUtils.isEmpty(maxBarId)) {
            return prefix + "0001";
        }
        String index = (Integer.valueOf(maxBarId.substring(1)) + 1) + "";
        if (index.length() < 4) {
            int padding = 4 - index.length();
            for (int i = 0; i < padding; i++) {
                index = "0" + index;
            }
        }
        return prefix + index;
    }

    private Long countBarHeat(BarProfile barProfile) {
        int interest = corgiUserFollowService.countFollowed(barProfile.getBarId());
        List<String> userIds = new ArrayList<>();
        Long duplicate = 0l;
        if (barProfile.getRange() != null && barProfile.getRange() > 0) {
            UserQuery userQuery = new UserQuery();
            userQuery.setLat(barProfile.getLat());
            userQuery.setLng(barProfile.getLng());
            userQuery.setRange(barProfile.getRange() / 1000.0);
            userIds = corgiUserService.getAllNearByUser(userQuery);
            duplicate = corgiBarMapper.countBarFollow(barProfile.getBarId(), String.join("','", userIds));
        }

        return interest + userIds.size() - duplicate;
    }
}
