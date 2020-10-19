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
import com.corgi.user.api.CorgiBarService;
import com.corgi.user.api.CorgiCouponService;
import com.corgi.user.api.CorgiUserFollowService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.*;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public List<BarProfile> getBarListByCity(String city) {
        return corgiBarMapper.getBarListByCity(city);
    }

    @Override
    public BarProfile getBarProfile(String barId) {
        BarProfile barProfile = corgiBarMapper.getBar(barId);
        if (barProfile != null) {
            barProfile.setHeat(countBarHeat(barProfile));
        }
        CorgiActivity corgiActivity = new CorgiActivity();
        corgiActivity.setUserId(barId);
        corgiActivity.setStatus(CorgiActivity.NOT_DELETED);
        barProfile.setActivityCount((int) corgiActivityService.countCorgiActivity(corgiActivity));
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
        String maxBarId = corgiBarMapper.getMaxBarId();
        barProfile.setBarId(createBarId(maxBarId));
        corgiBarMapper.addBar(barProfile);
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
        BarProfile barProfile =  corgiBarMapper.getBarByAccount(account, password);
        CorgiActivity corgiActivity = new CorgiActivity();
        corgiActivity.setUserId(barProfile.getBarId());
        corgiActivity.setStatus(CorgiActivity.NOT_DELETED);
        barProfile.setActivityCount((int) corgiActivityService.countCorgiActivity(corgiActivity));
        return barProfile;
    }

    private String createBarId(String maxBarId) {
        if (StringUtils.isEmpty(maxBarId)) {
            return "B0001";
        }
        String index = (Integer.valueOf(maxBarId.substring(1)) + 1) + "";
        if (index.length() < 4) {
            int padding = 4 - index.length();
            for (int i = 0; i < padding; i++) {
                index = "0" + index;
            }
        }
        return "B" + index;
    }

    private Long countBarHeat(BarProfile barProfile) {
        int interest = corgiUserFollowService.countFollowed(barProfile.getBarId());
        UserQuery userQuery = new UserQuery();
        userQuery.setLat(barProfile.getLat());
        userQuery.setLng(barProfile.getLng());
        userQuery.setRange(barProfile.getRange() / 1000.0);
        List<String> userIds = corgiUserService.getAllNearByUser(userQuery);
        Long duplicate = corgiBarMapper.countBarFollow(barProfile.getBarId(), String.join("','", userIds));
        return interest + userIds.size() - duplicate;
    }
}
