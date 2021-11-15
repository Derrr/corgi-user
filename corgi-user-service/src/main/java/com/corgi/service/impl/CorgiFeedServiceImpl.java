package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.*;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiFeedService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.api.CorgiVlogService;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import com.corgi.user.entity.UserProfile;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
    private CorgiVlogMapper corgiVlogMapper;

    @Autowired
    private CorgiUserActivityMapper corgiUserActivityMapper;

    @Override
    public List<String> getUnviewFeed(String userId, Integer size) {
        if (size == null || size > 10) {
            size = 10;
        }
        String index = UserUtils.getIndex(userId);
        CorgiVlog query = new CorgiVlog();
        query.setUserId(userId);
        query.setType(CorgiVlogHot.TYPE.MANUAL);
        query.setStatus("asc");
        List<CorgiVlog> corgiVlogs = corgiVlogMapper.recallHotVlog(query, 5, index);

        if (!CollectionUtils.isEmpty(corgiVlogs)) {
            if (corgiVlogs.size() < size) {
                size = 10 - corgiVlogs.size();
            } else {
                size = 0;
            }
            for (CorgiVlog vlog : corgiVlogs) {
                CorgiFeed feed = new CorgiFeed();
                feed.setFeed(vlog.getActivityId());
                feed.setFeedUserId(vlog.getUserId());
                feed.setUserId(userId);
                feed.setSource("manual");
                corgiFeedMapper.addFeed(feed, index);
            }
        }
        List<String> result = corgiFeedMapper.getUnviewFeed(userId, index, size);
        if (!CollectionUtils.isEmpty(corgiVlogs)) {
            for (CorgiVlog vlog : corgiVlogs) {
                result.add(0, vlog.getActivityId());
            }
        }
        if (result.size() >= size) {
            return result;
        }
        List<CorgiVlog> popularFeeds = this.getPopularFeeds(userId, size - result.size(), index);
        if (popularFeeds != null) {
            for (CorgiVlog vlog : popularFeeds) {
                CorgiFeed feed = new CorgiFeed();
                feed.setFeed(vlog.getActivityId());
                feed.setFeedUserId(vlog.getUserId());
                feed.setUserId(userId);
                feed.setSource("init");
                corgiFeedMapper.addFeed(feed, index);
                result.add(vlog.getActivityId());
            }
        }
        Integer max = size - result.size();
        if (max > 0) {
            CorgiVlogHot queryHot = new CorgiVlogHot();
            queryHot.setStatus(CorgiVlogHot.STATUS.OPEN);
            queryHot.setType(CorgiVlogHot.TYPE.AUTO);
            Integer total = corgiVlogMapper.countVlogHot(queryHot);
            Random random = new Random();
            for (int i = 0; i < max; i++) {
                String activityId = corgiVlogMapper.selectOneHot(random.nextInt(total));
                if (result.contains(activityId)) {
                    continue;
                }
                result.add(activityId);
            }
        }
        return result;
    }

    private List<CorgiVlog> getPopularFeeds(String userId, Integer size, String userIndex) {
        List<String> popularUserIds = corgiFeedMapper.getPopularUserIds();
        List<CorgiVlog> vlogs = new ArrayList<>();
        CorgiVlog recall = new CorgiVlog();
        recall.setUserId(userId);
        recall.setType("like");
        Random random = new Random();
        for (int i = 0; i < popularUserIds.size(); i++) {
            if (CollectionUtils.isEmpty(popularUserIds)) {
                break;
            }
            int index = random.nextInt(popularUserIds.size());
            String popularUserId = popularUserIds.get(index);
            List<CorgiVlog> vlogList = corgiVlogMapper.recallTargetVlog(popularUserId, recall, 1, userIndex);
            if (CollectionUtils.isEmpty(vlogList)) {
                popularUserIds.remove(index);
                continue;
            }
            vlogs.addAll(vlogList);
            if (vlogs.size() >= size) {
                break;
            }
            popularUserIds.remove(index);
        }
        return vlogs;
    }

    @Override
    public List<String> getFeedByActivityId(String activityId, String userId, Integer page, Integer size) {
        List<String> activityIds = corgiVlogMapper.recallActivityVlog(activityId, userId, (page - 1) * size, size);
        if (activityIds.size() < size) {
            Integer total = corgiVlogMapper.countActivityVlog(activityId, userId);
            if (total == null) {
                total = 0;
            }
            Integer start = page * size - total;
            if (start > 0) {
                activityIds.addAll(corgiVlogMapper.recallByActivityId(activityId, userId, start, size - activityIds.size()));
            }
        }
        return activityIds;
    }

    @Override
    public Integer countUnviewFeed(String userId) {
        return corgiFeedMapper.countUnviewFeed(userId, UserUtils.getIndex(userId));
    }

    @Override
    public void viewFeed(String userId, String feed, String source) {
        ActivityQuery query = new ActivityQuery();
        query.setActivityId(feed);
        List<CorgiActivity> activities = corgiUserActivityMapper.queryActivity(query);
        if (CollectionUtils.isEmpty(activities)) {
            return;
        }
        CorgiFeed feed1 = new CorgiFeed();
        feed1.setUserId(userId);
        feed1.setFeed(feed);
        feed1.setFeedUserId(activities.get(0).getId());
        feed1.setSource(source);
        corgiFeedMapper.addFeed(feed1, UserUtils.getIndex(userId));
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
    public void addBarFeed(CorgiFeed feed) {
        corgiFeedMapper.addBarFeed(feed);
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

    @Override
    public void deleteFeed(CorgiFeed feed) {
        for (int i = 0; i < 8; i++) {
            corgiFeedMapper.deleteFeed(feed, i + "");
        }
    }

    private CorgiFeed buildFeed(CorgiVlog vlog, String userId) {
        CorgiFeed feed = new CorgiFeed();
        feed.setFeed(vlog.getActivityId());
        feed.setFeedUserId(vlog.getUserId());
        feed.setUserId(userId);
        return feed;
    }

}
