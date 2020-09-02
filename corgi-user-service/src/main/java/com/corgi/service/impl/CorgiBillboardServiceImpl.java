package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiBillboardMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(sdf.format(calendar.getTime()));
        List<UserProfile> userProfiles = corgiBillboardMapper.getPopularUsers(userDetail, date, limit);
        return userProfiles;
    }

    @Override
    public List<UserProfile> getPassionUser(UserDetail userDetail, Integer limit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(sdf.format(calendar.getTime()));
        return corgiBillboardMapper.getPassionUsers(userDetail, date, limit);
    }

    @Override
    public List<UserProfile> getActiveUser(UserDetail userDetail, Integer limit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(sdf.format(calendar.getTime()));
        return corgiBillboardMapper.getActiveUsers(userDetail, date, limit);
    }

    @Override
    public void updateBillboardByNickname(String from, String to, String date) {
        corgiBillboardMapper.updateBillboardByNickname(from, to, date);
    }
}
