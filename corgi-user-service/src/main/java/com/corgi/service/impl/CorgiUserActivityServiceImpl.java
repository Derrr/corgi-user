package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.CorgiUserActivityMapper;
import com.corgi.mapper.CorgiVlogMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlogHot;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author tairanliu
 */
@Service
@Component
public class CorgiUserActivityServiceImpl implements CorgiUserActivityService {
    @Autowired
    private CorgiUserActivityMapper corgiUserActivityMapper;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiPicService corgiPicService;
    @Autowired
    private CorgiToolService corgiToolService;
    @Autowired
    private CorgiVlogMapper corgiVlogMapper;

    @Override
    public List<String> searchFeedActivity(ActivityQuery query) {
        if (!CollectionUtils.isEmpty(query.getGroup())) {
            query.setVersion(Strings.join(query.getGroup(), '|').replaceAll("'", "").replaceAll("\\|", "','"));
        }
        if (!CollectionUtils.isEmpty(query.getRole())) {
            query.setType(Strings.join(query.getRole(), '|').replaceAll("'", "").replaceAll("\\|", "','"));
        }
        if (query.getStartAge() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, query.getStartAge() * -1);
            query.setStartTime(sdf.format(calendar.getTime()));
        }
        if (query.getEndAge() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, query.getEndAge() * -1);
            query.setEndTime(sdf.format(calendar.getTime()));
        }
        return corgiUserActivityMapper.searchActivityFeed(query);
    }

    @Override
    public boolean signUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.addSignUp(userSignUp);
        return true;
    }

    @Override
    public boolean signOut(UserSignUp userSignUp) {
        corgiUserActivityMapper.deleteSignUp(userSignUp);
        return true;
    }

    @Override
    public boolean updateSignUp(UserSignUp userSignUp) {
        corgiUserActivityMapper.updateSignUp(userSignUp);
        return true;
    }

    @Override
    public void updateActivityStatus(String activityId, String status) {
        corgiUserActivityMapper.updateCheckStatus(activityId, status);
    }

    @Override
    public void deleteActivity(String activityId) {
        corgiUserActivityMapper.deleteSignUpByActivity(activityId);
        List<ActivityPic> pics = corgiPicService.getActivityPic(activityId);
        if (!CollectionUtils.isEmpty(pics)) {
            for (ActivityPic activity : pics) {
                corgiPicService.deleteActivityPic(activity.getPicId());
            }
        }
        corgiToolService.updateActivityTopic(activityId, null);
    }

    @Override
    public void deleteSignUpByActivity(String activityId) {
        corgiUserActivityMapper.deleteSignUpByActivity(activityId);
    }

    @Override
    public Integer getStatus(String userId, String activityId) {
        return corgiUserActivityMapper.getStatus(userId, activityId);
    }

    @Override
    public List<UserProfile> getUsers(String activityId, String userId, String status) {
        List<UserProfile> userProfiles = corgiUserActivityMapper.getUser(activityId, status);
        return corgiUserService.populateUserProfile(userProfiles, userId);
    }

    @Override
    public List<UserProfile> getPopularUsers(String activityId, String status) {
        List<UserProfile> userProfiles = corgiUserActivityMapper.getPopularUser(activityId, status);
        return corgiUserService.populateUserProfile(userProfiles, null);
    }

    @Override
    public Integer countUsers(String activityId, String status) {
        return corgiUserActivityMapper.countUser(activityId, status);
    }

    @Override
    public List<String> getFollowUserActivity(ActivityQuery query) {
        query.setPageSize(query.getPageSize() + 1);
        List<String> followActivityIds = corgiUserActivityMapper.getFollowedActivityIds(query);
        String startId = "";
        String endId = "";
        if (query.getPage() > 0) {
            if (followActivityIds.size() > 0) {
                startId = followActivityIds.get(0);
            } else {
                return new ArrayList<>();
            }
        }
        if (followActivityIds.size() >= query.getPageSize()) {
            endId = followActivityIds.get(followActivityIds.size() - 1);
            followActivityIds.remove(followActivityIds.size() - 1);
        }
        List<String> userActivityIds = corgiUserActivityMapper.getUserRangeActivityIds(query, startId, endId);
        followActivityIds = this.mergeActivityIds(followActivityIds, userActivityIds);
        return followActivityIds;
    }

    @Override
    public Integer countFollowUserActivity(ActivityQuery query) {
        return corgiUserActivityMapper.countFollowedActivityIds(query);
    }

    @Override
    public List<String> getSignUpActivity(String userId, Integer page, Integer pageSize) {
        return corgiUserActivityMapper.getSignUpActivityId(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<CorgiActivity> queryActivity(ActivityQuery query) {
        return corgiUserActivityMapper.queryActivity(query);
    }

    @Override
    public List<String> queryHotActivity(ActivityQuery query) {
        return corgiUserActivityMapper.queryHotActivity(query);
    }

    @Override
    public List<String> getHeatActivity(CorgiActivity corgiActivity, Integer page, Integer pageSize) {
        String category;
        if (corgiActivity.getBarId() != null) {
            category = corgiActivity.getCategory() + corgiActivity.getBarId();
        } else {
            category = "video','image','text";
        }
        String date = corgiActivity.getCreateTime();
        List<String> topics = corgiActivity.getTopics();
        String topic = null;
        if (!CollectionUtils.isEmpty(topics)) {
            topic = topics.get(0);
        }
        List<String> hashtags = corgiActivity.getHashtags();
        String hashtag = null;
        if (!CollectionUtils.isEmpty(hashtags)) {
            hashtag = hashtags.get(0);
        }
        return corgiUserActivityMapper.getHeadActivityPic(category, date, (page - 1) * pageSize, pageSize, topic, hashtag);
    }

    @Override
    public List<String> getCityBarActivity(CorgiActivity corgiActivity, Integer page, Integer pageSize) {
        return corgiUserActivityMapper.getCityBarActivity((page - 1) * pageSize, pageSize, corgiActivity.getCity());
    }

    @Override
    public void addActivityCreator(String userId, String activityId, String category) {
        corgiUserActivityMapper.addActivityCreator(activityId, userId, category);
    }

    @Override
    public void changeActivityCreator(String activityId, String status) {
        corgiUserActivityMapper.changeActivityCreator(activityId, status);
    }

    @Override
    public long countActivity(String date, String category) {
        return corgiUserActivityMapper.countActivity(category, date);
    }

    @Override
    public long countActivityUser(String date, String category) {
        return corgiUserActivityMapper.countActivityUser(category, date);
    }

    @Override
    public int countActivityDate(ActivityQuery query) {
        return corgiUserActivityMapper.countActivityDate(query);
    }

    @Override
    public long countSignUpUser(String date) {
        return corgiUserActivityMapper.countSignUpUser(date);
    }

    @Override
    public void deleteActivityCreator(String activityId) {
        corgiUserActivityMapper.deleteActivityCreator(activityId);
        CorgiVlogHot hot = new CorgiVlogHot();
        hot.setStatus("close");
        hot.setActivityId(activityId);
        corgiVlogMapper.updateVlogHot(hot);
//        CorgiFeed feed = new CorgiFeed();
//        feed.setFeed(activityId);
//        corgiFeedService.deleteFeed(feed);
    }

    @Override
    public List<String> getParticipateActivity(String userId, Integer page, Integer size) {
        return corgiUserActivityMapper.getParticipateActivity(userId, (page - 1) * size, size);
    }

    @Override
    public int countUserActivity(String userId) {
        return corgiUserActivityMapper.countUserActivity(userId);
    }

    private List<String> mergeActivityIds(List<String> activityIds, List<String> mergeIds) {
        for (String mergeId : mergeIds) {
            int i = 0;
            for (; i < activityIds.size(); i++) {
                String activityId = activityIds.get(i);
                if (mergeId.compareTo(activityId) > 0) {
                    break;
                }
            }
            activityIds.add(i, mergeId);
        }
        return activityIds;
    }
}
