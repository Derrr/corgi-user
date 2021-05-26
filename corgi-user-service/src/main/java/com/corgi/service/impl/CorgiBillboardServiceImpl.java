package com.corgi.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiBillboardMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<UserProfile> getPastBillboard(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date pastDate = sdf.parse(date);
            Date nowDate = new Date();
            if ((nowDate.getTime() - pastDate.getTime()) / (1000 * 3600 * 24) > 20) {
                return corgiBillboardMapper.getPastPopularBillboard(date);
            } else {
                return corgiBillboardMapper.getPastBillboard(date);
            }
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(calendar.getTime());
        List<UserProfile> userProfiles = corgiBillboardMapper.getPopularUsers(userDetail, date, limit);
        return userProfiles;
    }

    @Override
    public List<UserProfile> getPassionUser(UserDetail userDetail, Integer limit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        return corgiBillboardMapper.getPassionUsers(userDetail, date, limit);
    }

    @Override
    public List<UserProfile> getActiveUser(UserDetail userDetail, Integer limit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        return corgiBillboardMapper.getActiveUsers(userDetail, date, limit);
    }

    @Override
    public void updateBillboardByNickname(String from, String to, String date) {
        if (StringUtils.isNotEmpty(from) && StringUtils.isNotEmpty(to)) {
            corgiBillboardMapper.updateBillboard(from, to, date);
            redisTemplate.opsForValue().set("billboard_block_".concat(from), from, 30, TimeUnit.DAYS);
        }
    }

    @Override
    public void updateBillboardOrder(String userId, String date, Integer order) {
        corgiBillboardMapper.updateBillboardOrder(userId, date, order);
    }

    @Override
    public List<Billboard> getBillboardByDate(String startDate, String endDate) {
        List<Billboard> billboards = corgiBillboardMapper.getBillboardByDate(startDate, endDate);
        for (Billboard billboard : billboards) {
            if (billboard.getCount() > 1) {
                List<String> dates = corgiBillboardMapper.getBillboardTimeById(billboard.getUserId());
                if (dates.size() > 1) {
                    billboard.setLastDate(dates.get(1));
                }
            }
        }
        return billboards;
    }

    @Override
    public void addActivityBillboard(String activityId) {
        corgiBillboardMapper.addActivityBillboard(activityId);
        corgiBillboardMapper.addActivityBillboardStatus(activityId);
    }

    @Override
    public void deleteActivityBillboard(String activityId) {
        corgiBillboardMapper.deleteActivityBillboard(activityId);
    }

    @Override
    public List<String> getActivityBillboard() {
        return corgiBillboardMapper.getActivityBillboard();
    }

    @Override
    public List<ActivityBillboard> getAllActivityBillboard() {
        return corgiBillboardMapper.getAllActivityBillboard();
    }

    @Override
    public Integer countOnBoard(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return corgiBillboardMapper.countOnBoard(userId, sdf.format(new Date()));
    }

}
