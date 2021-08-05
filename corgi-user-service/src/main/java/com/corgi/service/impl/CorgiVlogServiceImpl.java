package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiVlogMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ge
 *
 * @author tairanliu
 */
@Service(interfaceClass = CorgiVlogService.class)
@Slf4j
@Component
public class CorgiVlogServiceImpl implements CorgiVlogService {
    @Autowired
    private CorgiVlogMapper corgiVlogMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Reference
    private CorgiFeedService corgiFeedService;

    @Override
    public CorgiVlog getVlog(String activityId) {
        return corgiVlogMapper.getVlogByActivityId(activityId);
    }

    @Override
    public CorgiVlog getVlogByVideoId(String videoId) {
        return corgiVlogMapper.getVlogById(videoId);
    }

    @Override
    public void deleteVlog(String activityId) {
        corgiVlogMapper.deleteVlog(activityId);
    }

    @Override
    public void addVlog(CorgiVlog corgiVlog) {
        corgiVlogMapper.addVlog(corgiVlog);
    }

    @Override
    public void addVlogCount(CorgiVlog corgiVlog) {
        corgiVlogMapper.addVlogCount(corgiVlog);
    }

    @Override
    public List<CorgiVlog> recallVlog(CorgiVlog corgiVlog, Integer limit) {
        List<CorgiVlog> vlogs = new ArrayList<>();
        if ("like".equals(corgiVlog.getType()) || "follow".equals(corgiVlog.getType())) {
            List<String> creatorIds = corgiVlogMapper.recallLikeUser(corgiVlog, limit, UserUtils.getIndex(corgiVlog.getUserId()));
            if (creatorIds != null) {
                for (String creatorId : creatorIds) {
                    vlogs.addAll(corgiVlogMapper.recallLikeVlog(corgiVlog, creatorId, 1, UserUtils.getIndex(corgiVlog.getUserId())));
                }
            }
            return vlogs;
        }
        if (Math.random() < 0.5) {
            vlogs = corgiVlogMapper.recallVlog(corgiVlog, limit, null, UserUtils.getIndex(corgiVlog.getUserId()));
            for (CorgiVlog vlog : vlogs) {
                vlog.setType("new|");
            }
        } else {
            vlogs = corgiVlogMapper.recallVlog(corgiVlog, limit, corgiVlog.getStatus(), UserUtils.getIndex(corgiVlog.getUserId()));
            for (CorgiVlog vlog : vlogs) {
                vlog.setType("city|");
            }
        }
        return vlogs;
    }

    @Override
    public List<CorgiVlog> recallTargetVlog(String targetId, CorgiVlog corgiVlog, Integer limit) {
        return corgiVlogMapper.recallTargetVlog(targetId, corgiVlog, limit, UserUtils.getIndex(corgiVlog.getUserId()));
    }

    @Override
    public List<CorgiVlog> recallHotVlog(CorgiVlog corgiVlog, Integer limit) {
        return corgiVlogMapper.recallHotVlog(corgiVlog, limit, UserUtils.getIndex(corgiVlog.getUserId()));
    }

    @Override
    public List<CorgiVlog> getFollowVlog(String userId, Integer page, Integer size) {
        return corgiVlogMapper.getFollowVlog(userId, (page - 1) * size, size, getNowDate());
    }

    @Override
    public List<CorgiVlog> getUserVlog(String userId, Integer page, Integer size) {
        return corgiVlogMapper.getUserVlog(userId, (page - 1) * size, size, getNowDate());
    }

    @Override
    public List<CorgiVlog> getTopicVlog(String topic, Integer page, Integer size) {
        return corgiVlogMapper.getUserVlog(topic, (page - 1) * size, size, getNowDate());
    }

    @Override
    public void addHotVlog(CorgiVlogHot corgiVlogHot) {
        corgiVlogMapper.addVlogHot(corgiVlogHot);
    }

    @Override
    public void updateHotVlog(CorgiVlogHot corgiVlogHot) {
        if (corgiVlogHot.getId() == null && corgiVlogHot.getActivityId() != null
                && corgiVlogHot.getLikeCount() != null && corgiVlogHot.getLikeCount() > 0) {
            CorgiVlogHot hot = new CorgiVlogHot();
            hot.setType(CorgiVlogHot.TYPE.AUTO);
            hot.setActivityId(corgiVlogHot.getActivityId());
            if (corgiVlogMapper.countVlogHot(hot) == 0) {
                CorgiVlog vlog = corgiVlogMapper.getVlogById(hot.getActivityId());
                if (vlog != null) {
                    hot.setExpectView(10);
                    corgiVlogMapper.addVlogHot(hot);
                }
            }
        }
        corgiVlogMapper.updateVlogHot(corgiVlogHot);
    }

    @Override
    public List<CorgiVlogHot> getHotVlog(CorgiVlogHot corgiVlogHot, Integer page, Integer pageSize) {
        return corgiVlogMapper.getVlogHot(corgiVlogHot, (page - 1) * pageSize, pageSize);
    }

    @Override
    public Integer countHotVlog(CorgiVlogHot corgiVlogHot) {
        return corgiVlogMapper.countVlogHot(corgiVlogHot);
    }

    @Override
    public Integer countVlog(String date) {
        return corgiVlogMapper.countVlogByDate(date);
    }

    @Override
    public CorgiVlog countByTopic(String topic) {
        return corgiVlogMapper.countByTopic(topic);
    }

    private String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
