package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiLikeMapper;
import com.corgi.mapper.CorgiUserActivityMapper;
import com.corgi.user.api.CorgiLikeService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserActivityService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.ActivityLike;
import com.corgi.user.entity.ActivityMessage;
import com.corgi.user.entity.UserDetail;
import com.corgi.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

@Service(interfaceClass = CorgiLikeService.class)
@Slf4j
@Component
public class CorgiLikeServiceImpl implements CorgiLikeService {
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiLikeMapper corgiLikeMapper;
    @Autowired
    private CorgiToolService corgiToolService;
    @Autowired
    private CorgiUserActivityService corgiUserActivityService;

    @Override
    public Integer addActivityLike(ActivityLike activityLike) {
        String userId = activityLike.getLikeUserId();
        if ("-1".equals(userId) || StringUtils.isEmpty(userId)) {
            userId = "1";
        }
        UserDetail userDetail = corgiUserService.getUserDetail(userId, null);
        activityLike.setLikeUserName(userDetail.getNickname());
        activityLike.setLikeUserAvatar(userDetail.getAvatar());

        Integer result = corgiLikeMapper.addActivityLike(activityLike);
        if (result > 0 && !userDetail.getUserId().equals(activityLike.getUserId())) {
            corgiToolService.addActivityMessage(ActivityMessage.builder()
                    .activityId(activityLike.getActivityId())
                    .fromUserAvatar(userDetail.getAvatar())
                    .fromUserId(userDetail.getUserId())
                    .fromUserName(userDetail.getNickname())
                    .toUserId(activityLike.getUserId())
                    .time(System.currentTimeMillis())
                    .messageType(ActivityMessage.LIKE)
                    .build());
        }
        return result;
    }

    @Override
    public Integer deleteActivityLike(String userId, String activityId) {
        Integer result = corgiLikeMapper.deleteActivityLike(userId, activityId);
        corgiToolService.deleteActivityMessageByMessage(ActivityMessage.builder()
                .fromUserId(userId)
                .activityId(activityId)
                .messageType(ActivityMessage.LIKE)
                .build());
        return result;
    }

    @Override
    public List<ActivityLike> getActivityLike(String activityId, Integer page, Integer pageSize) {
        List<ActivityLike> likeList = corgiLikeMapper.getActivityLike(activityId, (page - 1) * pageSize, pageSize);
        return this.populateLike(likeList);
    }

    @Override
    public Long countActivityLike(String activityId) {
        return corgiLikeMapper.countActivityLike(activityId);
    }

    @Override
    public Integer countRealActivityLike(String activityId) {
        return corgiLikeMapper.countRealActivityLike(activityId);
    }

    @Override
    public List<ActivityLike> getFollowUser(String userId, String activityId) {
        return corgiLikeMapper.getFollowUser(userId, activityId);
    }

    @Override
    public Integer countUserLike(String activityId, String userId) {
        return corgiLikeMapper.countUserLike(activityId, userId);
    }

    @Override
    public List<String> getLikedActivity(String userId, Integer page, Integer pageSize) {
        return corgiLikeMapper.getLikedActivityId(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public long countLikeByDate(String date, String category) {
        return corgiLikeMapper.countLikeByDate(date, category);
    }

    @Override
    public long countLikeUserByDate(String date, String category) {
        return corgiLikeMapper.countLikeUserByDate(date, category);
    }

    @Override
    public Integer countUserLikeByDate(String userId, String date) {
        return corgiLikeMapper.countUserLikeByDate(userId, date);
    }

    @Override
    public List<ActivityLike> getLikeByPage(Integer page, Integer pageSize) {
        return corgiLikeMapper.getLikeByPage((page - 1) * pageSize, pageSize);
    }

    @Override
    public List<ActivityLike> queryLike(ActivityLike query, Integer size) {
        List<ActivityLike> likeList = corgiLikeMapper.queryLike(query, size);
        return this.populateLike(likeList);
    }

    @Override
    public Double getAvgLike(String userId) {
        Integer ac = corgiUserActivityService.countUserActivity(userId);
        if (ac < 2) {
            return 4.0;
        }
        Integer lc = corgiLikeMapper.countGetLiked(userId);
        return lc * 1.0 / ac;
    }

    private List<ActivityLike> populateLike(List<ActivityLike> likeList) {
        if (!CollectionUtils.isEmpty(likeList)) {
            Long now = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ActivityLike like : likeList) {
                like.setTimeShow(TimeUtil.buildTimeText(like.getCtime(), now, sdf));
            }
        }
        return likeList;
    }
}
