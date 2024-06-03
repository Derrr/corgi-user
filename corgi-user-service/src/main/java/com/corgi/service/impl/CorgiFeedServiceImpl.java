package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.*;
import com.corgi.user.api.CorgiFeedService;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.api.CorgiVlogService;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiUserGoods;
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
    private CorgiUserActivityMapper corgiUserActivityMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Reference
    private CorgiVlogService corgiVlogService;
    @Reference
    private CorgiUserService corgiUserService;
    @Reference
    private CorgiOrderService corgiOrderService;


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
            if (size < 0) {
                return manuallyIds;
            }
        } else {
            manuallyIds = new ArrayList<>();
        }
        List<String> likeIds = getLikeRecommend(userId, index, 3);
        if (!CollectionUtils.isEmpty(likeIds)) {
            size = size - likeIds.size();
            manuallyIds.addAll(likeIds);
            if (size < 0) {
                return manuallyIds;
            }
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

//        List<CorgiVlog> popularFeeds = this.getPopularFeeds(userId, size - result.size());
//
//        if (popularFeeds != null) {
//            for (CorgiVlog vlog : popularFeeds) {
//                CorgiFeed feed = new CorgiFeed();
//                feed.setFeed(vlog.getActivityId());
//                feed.setFeedUserId(vlog.getUserId());
//                feed.setUserId(userId);
//                feed.setSource("init");
//                corgiFeedMapper.addFeed(feed, index);
//                result.add(vlog.getActivityId());
//            }
//        }
        Integer max = size - result.size();
        if (max > 0) {
            CorgiVlogHot queryHot = new CorgiVlogHot();
            queryHot.setStatus(CorgiVlogHot.STATUS.OPEN);
            queryHot.setType(CorgiVlogHot.TYPE.AUTO);
            queryHot.setLikeCount(120);
            Integer total = corgiVlogMapper.countVlogHot(queryHot);
            Random random = new Random();
            for (int i = 0; i < max; i++) {
                String activityId = corgiVlogMapper.selectOneHot(random.nextInt(total));
                if (result.contains(activityId)) {
                    continue;
                }
                result.add(0, activityId);
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

    private List<CorgiVlog> getPopularFeeds(String userId, Integer size) {
        String groups = null;
        List<String> groupList = corgiUserService.getPreferGroup(userId);
        if (!CollectionUtils.isEmpty(groupList)) {
            groups = String.join("','", groupList);
        }
        CorgiVlog recall = new CorgiVlog();
        recall.setActivityId(groups);
        recall.setUserId(userId);
        recall.setType(CorgiVlogHot.TYPE.AUTO);
        recall.setStatus("desc");
        List<CorgiVlog> vlogs = corgiVlogService.recallHotVlog(recall, size);
        return vlogs;
    }

    @Override
    public List<String> getFeedByActivityId(String activityId, String category, String userId, Integer page, Integer size) {
        if (StringUtils.isEmpty(category)) {
            category = CorgiActivity.CAT_IMAGE;
        }
        String key = "feed_activity_" + activityId + "-" + category;
        List<String> activityIds = redisTemplate.opsForList().range(key, 0, -1);
        if (!CollectionUtils.isEmpty(activityIds)) {
            return activityIds;
        }
        if (CorgiActivity.CAT_IMAGE.equals(category)) {
            String creatorId = corgiUserActivityMapper.getCreator(activityId);
            CorgiUserGoods goods = new CorgiUserGoods();
            goods.setGoodsType(CorgiUserGoods.GOODS_TYPE.ACTIVITY);
            goods.setSize(3);
            goods.setTraderId(creatorId);
            List<CorgiUserGoods> userGoods = corgiOrderService.getHotGoods(goods);
            activityIds = corgiVlogMapper.getUserActivity(activityId, size);
            if (!CollectionUtils.isEmpty(userGoods) && !CollectionUtils.isEmpty(activityIds)) {
                List<String> goodsIds = userGoods.stream().map(g -> g.getGoodsId()).collect(Collectors.toList());
                redisTemplate.delete(key);
                redisTemplate.opsForList().leftPushAll(key, activityIds);
                redisTemplate.expire(key, 1l, TimeUnit.DAYS);
                Iterator<String> it = activityIds.iterator();
                while (it.hasNext()) {
                    String activityId1 = it.next();
                    if (goodsIds.contains(activityId1)) {
                        it.remove();
                    }
                }
                activityIds.addAll(0, goodsIds);
            }
        }
        if (size > activityIds.size()) {
            String lastId = null;
            CorgiVlogHot hot = new CorgiVlogHot();
            hot.setActivityId(activityId);
            List<CorgiVlogHot> hots = corgiVlogMapper.getVlogHot(hot, 0, 1);
            if (!CollectionUtils.isEmpty(hots)) {
                lastId = hots.get(0).getId() + "";
            }
            CorgiVlog recall = new CorgiVlog();
            recall.setUserId(userId);
            recall.setCategory(category);
            recall.setType(CorgiVlogHot.TYPE.AUTO);
            recall.setStatus("asc");
            List<CorgiVlog> vlogs = corgiVlogMapper.recallHotVlog(recall, lastId, size, null);
            for (CorgiVlog vlog : vlogs) {
                if (!activityIds.contains(vlog.getActivityId()) && !StringUtils.isEmpty(vlog.getActivityId())) {
                    activityIds.add(vlog.getActivityId());
                    if (size <= activityIds.size()) {
                        break;
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(activityIds)) {
            return new ArrayList<>();
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
        if (!StringUtils.isEmpty(feed.getUserId())) {
            corgiFeedMapper.deleteFeedByUserId(feed.getUserId(), UserUtils.getIndex(feed.getUserId()));
        } else {
            for (int i = 0; i < 8; i++) {
                corgiFeedMapper.deleteFeed(feed, i + "");
            }
        }
    }

    @Override
    public Integer countFeed(String activityId, String userId) {
        return corgiFeedMapper.countActivityFeed(activityId, userId, UserUtils.getIndex(userId));
    }

    private CorgiFeed buildFeed(CorgiVlog vlog, String userId) {
        CorgiFeed feed = new CorgiFeed();
        feed.setFeed(vlog.getActivityId());
        feed.setFeedUserId(vlog.getUserId());
        feed.setUserId(userId);
        return feed;
    }

    private List<String> getLikeRecommend(String userId, String index, Integer size) {
        String likeKey = userId + "-recommend-activity";
        List<String> likeIds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String activity = redisTemplate.opsForList().rightPop(likeKey);
            if (StringUtils.isEmpty(activity)) {
                break;
            }
            String[] activityParam = activity.split("-");
            CorgiFeed feed = new CorgiFeed();
            feed.setFeed(activityParam[0]);
            if (activityParam.length > 1) {
                feed.setFeedUserId(activityParam[1]);
            }
            feed.setUserId(userId);
            feed.setSource("likeRecommend");
            int j = corgiFeedMapper.addFeed(feed, index);
            if (j > 0) {
                likeIds.add(activityParam[0]);
                log.info("feed like recommend...{}", activityParam[0]);
            }
        }
        return likeIds;
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
        String manualKey = "manual_feed_" + userId;
        List<String> manualIds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String activity = redisTemplate.opsForList().leftPop(manualKey);
            if (StringUtils.isEmpty(activity)) {
                break;
            }
            String[] activityParam = activity.split("-");
            CorgiFeed feed = new CorgiFeed();
            feed.setFeed(activityParam[0]);
            if (activityParam.length > 1) {
                feed.setFeedUserId(activityParam[1]);
            }
            feed.setUserId(userId);
            feed.setSource("manual");
            int j = corgiFeedMapper.addFeed(feed, index);
            if (j > 0) {
                manualIds.add(activityParam[0]);
            }
        }
        if (!StringUtils.isEmpty(userFeeds)) {
            manualIds.add(userFeeds);
        }
        return manualIds;
    }

}
