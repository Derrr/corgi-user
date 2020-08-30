package com.corgi.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiBillboardMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiBillboardService.class)
@Slf4j
@Component
public class CorgiBillboardServiceImpl implements CorgiBillboardService {
    @Autowired
    private CorgiBillboardMapper corgiBillboardMapper;
    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<UserProfile> getPastBillboard(String date) {
        return corgiBillboardMapper.getPastBillboard(date);
    }

    @Override
    public List<UserProfile> getBillboard(String date) {
        List<UserProfile> userProfiles = corgiBillboardMapper.getBillboardUsers(date);
        for (UserProfile userProfile : userProfiles) {
            userProfile.setPics(corgiPicService.getUserPic(userProfile.getUserId()));
        }
        return userProfiles;
    }

    @Override
    public void addBillboard(UserProfile userProfile, String date, String countType) {
        corgiBillboardMapper.addBillboardUser(userProfile.getUserId(), userProfile.getMatch().intValue(), countType, date);
    }

    @Override
    public void cleanBillboard(String date) {
        corgiBillboardMapper.cleanBillboardByDate(date);
    }

    @Override
    public List<UserProfile> getPopularUser(UserDetail userDetail, Integer limit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "%";
        List<UserProfile> userProfiles = corgiBillboardMapper.getPopularUsers(userDetail, date, limit);
        return userProfiles;
    }

    @Override
    public List<UserProfile> getPassionUser(UserDetail userDetail, Integer limit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "%";
        return corgiBillboardMapper.getPassionUsers(userDetail, date, limit);
    }

    @Override
    public List<UserProfile> getActiveUser(UserDetail userDetail, Integer limit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "%";
        return corgiBillboardMapper.getActiveUsers(userDetail, date, limit);
    }

    @Override
    public void updateBillboardByNickname(String from, String to, String date) {
        String fromId = corgiBillboardMapper.getUserIdByNickname(from);
        String toId = corgiBillboardMapper.getUserIdByNickname(to);
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            corgiBillboardMapper.updateBillboard(from, to, date);
            redisTemplate.opsForValue().set("billboard_block_".concat(fromId), from, 30, TimeUnit.DAYS);
        }
    }
}
