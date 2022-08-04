package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.*;
import com.corgi.user.api.CorgiFeedService;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private StringRedisTemplate redisTemplate;

    @Override
    public List<String> getFollowedFeed(String userId, Integer size) {
        return null;
    }

    @Override
    public List<String> getUnviewFeed(String userId, Integer size) {
        if (size == null || size > 10) {
            size = 10;
        }
        String index = UserUtils.getIndex(userId);
        List<String> manuallyIds = getManuallyRecommend(userId, index, 5);

        if (!CollectionUtils.isEmpty(manuallyIds)) {
            size = size - manuallyIds.size();
            size = size < 0 ? 0 : size;
        }
        List<String> result = corgiFeedMapper.getUnviewFeed(userId, index, size, null);
        if (!CollectionUtils.isEmpty(manuallyIds)) {
            for (String activityId : manuallyIds) {
                if (!result.contains(activityId)) {
                    result.add(0, activityId);
                }
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

    @Override
    public List<String> getPopularFeed(String userId, Integer size) {
        String index = UserUtils.getIndex(userId);
        List<String> result = new ArrayList<>();
        CorgiVlog query = new CorgiVlog();
        query.setUserId(userId);
        query.setType(CorgiVlogHot.TYPE.MANUAL);
        query.setStatus("new");
        List<CorgiVlog> corgiVlogs = corgiVlogMapper.recallHotVlog(query, null, size, index);
        if (corgiVlogs != null) {
            for (CorgiVlog vlog : corgiVlogs) {
                CorgiFeed feed = new CorgiFeed();
                feed.setFeed(vlog.getActivityId());
                feed.setFeedUserId(vlog.getUserId());
                feed.setUserId(userId);
                feed.setSource("manual");
                corgiFeedMapper.addFeed(feed, index);
                result.add(vlog.getActivityId());
            }
        }
        return result;
    }

    @Override
    public List<String> searchFeed(ActivityQuery query) {
//        List<String> oldResult = corgiFeedMapper.getUnviewFeed(query.getUserId(), UserUtils.getIndex(query.getUserId()), query.getPageSize(), "search");
//        if (oldResult.size() >= query.getPageSize()) {
//            return oldResult;
//        }
//        if (oldResult == null) {
//            oldResult = new ArrayList<>();
//        }
//        query.setPageSize(query.getPageSize() - oldResult.size());
        CorgiVlog vlogQuery = new CorgiVlog();
        vlogQuery.setStatus("1".equals(query.getType()) ? "verify" : query.getType());
        vlogQuery.setType(CorgiVlogHot.TYPE.AUTO);
        vlogQuery.setUserId(query.getUserId());
        if (!CollectionUtils.isEmpty(query.getGroup())) {
            vlogQuery.setActivityId(Strings.join(query.getGroup(), '|').replaceAll("'", "").replaceAll("\\|", "','"));
        }
        if (!CollectionUtils.isEmpty(query.getRole())) {
            vlogQuery.setVideoId(Strings.join(query.getRole(), '|').replaceAll("'", "").replaceAll("\\|", "','"));
        }
        if (query.getStartAge() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, query.getStartAge() * -1);
            vlogQuery.setCtime(sdf.format(calendar.getTime()));
        }
        if (query.getEndAge() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, query.getEndAge() * -1);
            vlogQuery.setUptime(sdf.format(calendar.getTime()));
        }
        String key = "search_feed_" + query.getUserId();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            String newstr = base64en.encode(md5.digest(query.toString().getBytes("utf-8")));
            key = "search_feed_" + newstr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<CorgiVlog> resultVlogs = corgiVlogMapper.recallHotVlog(vlogQuery, redisTemplate.opsForValue().get(key), query.getPageSize(), null);
        if (CollectionUtils.isEmpty(resultVlogs)) {
            redisTemplate.delete(key);
            return new ArrayList<>();
        }
        redisTemplate.opsForValue().set(key, resultVlogs.get(resultVlogs.size() - 1).getId().toString(), 20L, TimeUnit.HOURS);
        return resultVlogs.stream().map(v -> v.getActivityId()).collect(Collectors.toList());
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
        String key = "feed_activity_" + activityId;
        List<String> activityIds = redisTemplate.opsForList().range(key, 0, -1);
        if (!CollectionUtils.isEmpty(activityIds)) {
            return activityIds;
        }
        activityIds = corgiVlogMapper.getUserActivity(activityId, size);
        if (activityIds.size() < size) {
            List<String> tmpActivityIds = corgiVlogMapper.recallActivityVlog(activityId, userId, 0, size);
            for (String activityIdTmp : tmpActivityIds) {
                if (!activityIds.contains(activityIdTmp)) {
                    activityIds.add(activityIdTmp);
                    if (size <= activityIds.size()) {
                        break;
                    }
                }
            }
        }
        if (size > activityIds.size()) {
            CorgiVlog recall = new CorgiVlog();
            recall.setUserId(userId);
            recall.setType(CorgiVlogHot.TYPE.AUTO);
            recall.setStatus("asc");
            List<CorgiVlog> vlogs = corgiVlogMapper.recallHotVlog(recall, null, size, null);
            for (CorgiVlog vlog : vlogs) {
                if (!activityIds.contains(vlog.getActivityId())) {
                    activityIds.add(vlog.getActivityId());
                    if (size <= activityIds.size()) {
                        break;
                    }
                }
            }
        }
        redisTemplate.opsForList().rightPushAll(key, activityIds);
        redisTemplate.expire(key, 20L, TimeUnit.HOURS);
        return activityIds;
    }

    @Override
    public Integer countUnviewFeed(String userId) {
        return corgiFeedMapper.countUnviewFeed(userId, UserUtils.getIndex(userId));
    }

    @Override
    public void viewFeed(CorgiFeed feed) {
        String userId = feed.getUserId();
        //corgiFeedMapper.addFeed(feed, UserUtils.getIndex(userId));
        corgiFeedMapper.viewFeed(userId, feed.getFeed(), UserUtils.getIndex(userId));
        CorgiVlogHot hot = new CorgiVlogHot();
        hot.setViewCount(1);
        hot.setActivityId(feed.getFeed());
        corgiVlogMapper.updateVlogHot(hot);
        CorgiVlog vlog = new CorgiVlog();
        vlog.setViewCount(1l);
        vlog.setActivityId(feed.getFeed());
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

    private List<String> getManuallyRecommend(String userId, String index, Integer size) {
        String userFeeds = redisTemplate.opsForValue().get("vip_feed_" + userId);
        if (!StringUtils.isEmpty(userFeeds)) {
            CorgiFeed feed = new CorgiFeed();
            feed.setFeed(userFeeds);
            feed.setFeedUserId(userId);
            feed.setUserId(userId);
            feed.setSource("vip");
            int i = corgiFeedMapper.addFeed(feed, index);
            if (i > 0) {
                size--;
            } else {
                userFeeds = "";
            }
        }
        List<String> tmpIds = new ArrayList<>();
        try {
            tmpIds = redisTemplate.opsForList().range("manual_feed_" + userId, 0, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
//        if (CollectionUtils.isEmpty(tmpIds)) {
//            tmpIds = new ArrayList<>();
//            CorgiVlog query = new CorgiVlog();
//            query.setUserId(userId);
//            query.setType(CorgiVlogHot.TYPE.MANUAL);
//            query.setStatus("asc");
//            List<CorgiVlog> corgiVlogs = corgiVlogMapper.recallHotVlog(query, null, size, index);
//            if (!CollectionUtils.isEmpty(corgiVlogs)) {
//                for (CorgiVlog vlog : corgiVlogs) {
//                    tmpIds.add(vlog.getActivityId() + "-" + vlog.getUserId());
//                }
//            }
//        }
        List<String> manualIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tmpIds)) {
            for (String activity : tmpIds) {
                String[] activityParam = activity.split("-");
                CorgiFeed feed = new CorgiFeed();
                feed.setFeed(activityParam[0]);

                if (activityParam.length > 1) {
                    feed.setFeedUserId(activityParam[1]);
                }
                feed.setUserId(userId);
                feed.setSource("manual");
                int i = corgiFeedMapper.addFeed(feed, index);
                if (i > 0) {
                    manualIds.add(activityParam[0]);
                }
            }
        }
        if (!StringUtils.isEmpty(userFeeds)) {
            manualIds.add(userFeeds);
        }
        return manualIds;
    }

}
