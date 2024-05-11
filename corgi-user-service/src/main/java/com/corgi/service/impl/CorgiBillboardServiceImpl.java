package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.CorgiBillboardMapper;
import com.corgi.user.api.CorgiBillboardService;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
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
            if ((nowDate.getTime() - pastDate.getTime()) / (1000 * 3600 * 24) > 120) {
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
            if (StringUtils.isNotEmpty(userProfile.getAvatar()) && !"check".equals(userProfile.getAvatarCheckStatus())) {
                userProfile.setAvatar(userProfile.getAvatar().replaceAll("corgi-pic\\.oss-cn-beijing\\.aliyuncs\\.com", "image.corgi.org.cn").replaceAll("https://","http://"));
            }
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
        calendar.add(Calendar.DATE, -3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(calendar.getTime());
        List<UserProfile> userProfiles = corgiBillboardMapper.getPopularUsers(userDetail, date, limit);
        return userProfiles;
    }

    @Override
    public List<CorgiActivity> getPopularActivity(ActivityQuery activityQuery, Integer limit) {
        return corgiBillboardMapper.getPopularActivity(activityQuery, limit);
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
    public void updateBillboardOrder(String activityId, String date, Integer order) {
        corgiBillboardMapper.updateBillboardOrder(activityId, date, order);
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
    public void addActivityBillboard(ActivityBillboard activity) {
        corgiBillboardMapper.addActivityBillboard(activity);
        corgiBillboardMapper.addActivityBillboardStatus(activity);
    }

    @Override
    public void deleteActivityBillboard(ActivityBillboard activityBillboard) {
        corgiBillboardMapper.deleteActivityBillboard(activityBillboard);
    }

    @Override
    public List<String> getActivityBillboard(String date) {
        return corgiBillboardMapper.getActivityBillboard(date);
    }

    @Override
    public List<ActivityBillboard> getAllActivityBillboard(ActivityBillboard activityBillboard) {
        List<ActivityBillboard> activityBillboards = corgiBillboardMapper.getAllActivityBillboard(activityBillboard);
        if (CollectionUtils.isNotEmpty(activityBillboards)) {
            for (ActivityBillboard activityBillboard1 : activityBillboards) {
                activityBillboard1.setCtime(corgiBillboardMapper.getOnboardDate(activityBillboard1.getUserId(), activityBillboard1.getDate()));
            }
        }
        return activityBillboards;
    }

    @Override
    public Integer countOnBoard(String userId) {
        return corgiBillboardMapper.countOnBoard(userId);
    }

    @Override
    public PaidBillboard createPaidBillboard(PaidBillboard paidBillboard) {
        corgiBillboardMapper.addPaidBillboard(paidBillboard);
        return paidBillboard;
    }

    @Override
    public void updatePaiBillboard(PaidBillboard paidBillboard) {
        corgiBillboardMapper.updatePaidBillboard(paidBillboard);
    }

    @Override
    public Integer countPaiBillboard(PaidBillboard paidBillboard) {
        return corgiBillboardMapper.countPaidBillboard(paidBillboard);
    }

    @Override
    public List<PaidBillboard> queryPaidBillboard(PaidBillboard paidBillboard, Integer page, Integer size) {
        return corgiBillboardMapper.getPaidBillboard(paidBillboard, (page - 1) * size, size);
    }

    @Override
    public List<TopicBillboard> listTopicBillboard(TopicBillboard billboard) {
        return corgiBillboardMapper.listTopicBillboard(billboard);
    }

    @Override
    public void deleteTopicBillboard(TopicBillboard billboard) {
        corgiBillboardMapper.deleteTopicBillboard(billboard);
    }

    @Override
    public void updateTopicBillboard(TopicBillboard billboard) {
        corgiBillboardMapper.updateTopicBillboard(billboard);
    }

    @Override
    public void addTopicBillboard(TopicBillboard billboard) {
        corgiBillboardMapper.addTopicBillboard(billboard);
    }

}
