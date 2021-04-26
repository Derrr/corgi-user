package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiFeedMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiVlogMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiFeedService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiFeedService.class)
@Slf4j
@Component
public class CorgiFeedServiceImpl implements CorgiFeedService {

    @Autowired
    private CorgiFeedMapper corgiFeedMapper;

    @Autowired
    private CorgiUserMapper corgiUserMapper;

    @Autowired
    private CorgiVlogMapper corgiVlogMapper;

    @Override
    public List<String> getUnviewFeed(String userId, Integer size) {
        if (size == null) {
            size = 10;
        }
        String index = UserUtils.getIndex(userId);
        List<String> result = corgiFeedMapper.getUnviewFeed(userId, index, size);
        if (result.size() >= size) {
            return result;
        }
        List<CorgiVlog> popularFeeds = corgiVlogMapper.getPopularVlog(userId, index, size - result.size());
        if (popularFeeds != null) {
            for (CorgiVlog vlog : popularFeeds) {
                CorgiFeed feed = new CorgiFeed();
                feed.setFeed(vlog.getActivityId());
                feed.setFeedUserId(vlog.getUserId());
                feed.setUserId(userId);
                corgiFeedMapper.addFeed(feed, index);
                result.add(vlog.getActivityId());
            }
        }
        Integer max = size - result.size();
        if (max > 0) {
            Integer total = corgiVlogMapper.countVlogHot(new CorgiVlogHot());
            Random random = new Random();
            result = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                String activityId = corgiVlogMapper.selectOneHot(random.nextInt(total));
                result.add(activityId);
            }
        }
        return result;
    }

    @Override
    public List<String> getFeedByActivityId(String activityId, String userId, Integer page, Integer size) {
        List<String> activityIds = corgiVlogMapper.recallActivityVlog(activityId, userId, (page - 1) * size, size);
        if (activityIds.size() < size) {
            Integer total = corgiVlogMapper.countVlogHot(new CorgiVlogHot());
            Random random = new Random();
            for (int i = 0; i < size - activityIds.size(); i++) {
                String activityId1 = corgiVlogMapper.selectOneHot(random.nextInt(total));
                activityIds.add(activityId1);
            }
        }
        return activityIds;
    }

    @Override
    public Integer countUnviewFeed(String userId) {
        return corgiFeedMapper.countUnviewFeed(userId, UserUtils.getIndex(userId));
    }

    @Override
    public void viewFeed(String userId, String feed) {
        corgiFeedMapper.viewFeed(userId, feed, UserUtils.getIndex(userId));
        CorgiVlogHot hot = new CorgiVlogHot();
        hot.setViewCount(1);
        hot.setActivityId(feed);
        corgiVlogMapper.updateVlogHot(hot);
        CorgiVlog vlog = new CorgiVlog();
        vlog.setViewCount(1);
        vlog.setActivityId(feed);
        corgiVlogMapper.addVlogCount(vlog);
    }

    @Override
    public void addFeed(CorgiFeed corgiFeed) {
        if (corgiFeed != null && corgiFeed.hasValue()) {
            corgiFeedMapper.addFeed(corgiFeed, UserUtils.getIndex(corgiFeed.getUserId()));
        }
    }

    @Override
    public Integer countViewFeed(String date) {
        Integer total = 0;
        for (int i = 0; i < 8; i++) {
            total += corgiFeedMapper.countViewFeed(date, i + "");
        }
        return total;
    }


    private CorgiFeed buildFeed(CorgiVlog vlog, String userId) {
        CorgiFeed feed = new CorgiFeed();
        feed.setFeed(vlog.getActivityId());
        feed.setFeedUserId(vlog.getUserId());
        feed.setUserId(userId);
        return feed;
    }

}
