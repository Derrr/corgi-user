package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiStatistic;
import com.corgi.mapper.CorgiStatisticMapper;
import com.corgi.user.api.CorgiStatisticService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiStatisticService.class)
@Slf4j
@Component
public class CorgiStatisticServiceImpl implements CorgiStatisticService {
    @Autowired
    private CorgiStatisticMapper corgiStatisticMapper;

    @Override
    public void addCount(String table, String date, Long count) {
        corgiStatisticMapper.deleteCount(table, date);
        corgiStatisticMapper.addCount(table, date, count);
    }

    @Override
    public List<CorgiStatistic> getCount(String table, String beginDate, String endDate) {
        return corgiStatisticMapper.getCount(table, beginDate, endDate);
    }

    @Override
    public long sumCount(String table, String beginDate, String endDate) {
        if ("follow".equals(table)) {
            return corgiStatisticMapper.getRealFollow(beginDate);
        }
        if ("fans".equals(table)) {
            return corgiStatisticMapper.getRealFans(beginDate);
        }
        Long count = corgiStatisticMapper.sumCount(table, beginDate, endDate);
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    @Override
    public List<HashMap> getMap(String table, String beginDate, String endDate) {
        return corgiStatisticMapper.getMap(table, beginDate, endDate);
    }

    @Override
    public void initMap(String table, String date) {
        corgiStatisticMapper.initMap(date, table);
    }

    @Override
    public void updateMap(String table, String date, String key, Long count) {
        List<HashMap> map = corgiStatisticMapper.getMap(table, date, date);
        if (CollectionUtils.isEmpty(map)) {
            corgiStatisticMapper.initMap(date, table);
        }
        corgiStatisticMapper.updateMap(table, date, key, count);
    }

    @Override
    public void addList(String table, String date, String name, Long count) {
        corgiStatisticMapper.addList(table, date, name, count);
    }

    @Override
    public List<HashMap> getList(String table, String beginDate, String endDate) {
        List<CorgiStatistic> results = corgiStatisticMapper.getList(table, beginDate, endDate);
        if (CollectionUtils.isEmpty(results)) {
            return new ArrayList<>();
        }
        HashMap<String, HashMap> hashResult = new HashMap<>();
        for (CorgiStatistic corgiStatistic : results) {
            String date = corgiStatistic.getDate();
            HashMap dateResult = hashResult.get(corgiStatistic.getDate());
            if (dateResult == null) {
                dateResult = new HashMap();
                dateResult.put("date", date);
            }
            dateResult.put(corgiStatistic.getName(), corgiStatistic.getCount());
            hashResult.put(date, dateResult);
        }
        return new ArrayList<>(hashResult.values());
    }

    @Override
    public void addUserStay(String date, String registerDate, String stayCount, Long count) {
        corgiStatisticMapper.addUserStay(date, registerDate, stayCount, count);
    }

    @Override
    public List<HashMap> getUserStay(String beginDate, String endDate, String stayCount) {
        List<HashMap> stays = corgiStatisticMapper.getUserStay(beginDate, endDate, stayCount);
        return stays;
    }

    @Override
    public List<UserTrace> getUserTrace(String beginDate, String endDate, String userId) {
        return corgiStatisticMapper.getUserTrace(beginDate, endDate, userId);
    }

    @Override
    public List<UserTrace> getLastUserTrace(String userId) {
        return corgiStatisticMapper.getLastUserTrace(userId);
    }

    @Override
    public void addUserTrace(UserTrace userTrace) {
        corgiStatisticMapper.addUserTrace(userTrace);
    }

    @Override
    public void updateTraceStatus(UserTrace userTrace) {
        corgiStatisticMapper.updateTraceStatus(userTrace);
    }

    @Override
    public void updateTraceStay(UserTrace userTrace) {
        corgiStatisticMapper.updateTraceStay(userTrace);
    }

    @Override
    public List<HashMap> countUserTrace(String date) {
        return corgiStatisticMapper.countUserTrace(date);
    }

    @Override
    public Double countTotalUserTrace(String date) {
        return corgiStatisticMapper.countTotalUserTrace(date);
    }

    @Override
    public void addUserTraceSum(String date, String type, Double time) {
        corgiStatisticMapper.addUserTraceSum(type, date, time);
    }

    @Override
    public List<HashMap> getUserTraceSum(String beginDate, String endDate) {
        return corgiStatisticMapper.getUserTraceSum(beginDate, endDate);
    }

    @Override
    public void addCharacter(String openId, String character) {
        String ctr = corgiStatisticMapper.getCharacter(openId);
        if (StringUtils.isEmpty(ctr)) {
            corgiStatisticMapper.addCharacter(openId, character);
        } else {
            corgiStatisticMapper.updateCharacter(openId, character);
        }
    }

    @Override
    public CorgiBehaviorStatistics getBehaviorData(CorgiBehaviorReq behaviorReq) {
        if (behaviorReq.isPayType()) {
            return corgiStatisticMapper.getPayTypeBehavior(behaviorReq);
        }
        if (behaviorReq.isActivityType()) {
            return corgiStatisticMapper.getActivityTypeBehavior(behaviorReq,
                    CorgiBehaviorReq.ActivityType.valueOf(behaviorReq.getType()).getUser());
        }
        return null;
    }

    @Override
    public CorgiContentStatistics getContentData(CorgiContentReq contentReq) {
        switch (contentReq.getType()) {
            case CorgiContentReq.COMMENT:
                return corgiStatisticMapper.getMostComment(contentReq);
            case CorgiContentReq.FOLLOW:
                return corgiStatisticMapper.getMostFollow(contentReq);
            case CorgiContentReq.LIKE:
                return corgiStatisticMapper.getMostLike(contentReq);
            case CorgiContentReq.SHARE:
                return corgiStatisticMapper.getMostShare(contentReq);
            case CorgiContentReq.USER_LIKE:
                return corgiStatisticMapper.getMostUserLike(contentReq);
        }
        return null;
    }


}
