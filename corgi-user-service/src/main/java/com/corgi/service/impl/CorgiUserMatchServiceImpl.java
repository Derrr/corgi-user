package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiUserMatchMapper;
import com.corgi.support.MatchSupporter;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tairanliu
 */
@Slf4j
@Service(interfaceClass = CorgiUserMatchService.class)
@Component
public class CorgiUserMatchServiceImpl implements CorgiUserMatchService {
    @Autowired
    private CorgiUserMatchMapper userMatchMapper;
    @Autowired
    private CorgiUserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Double calculateUserMatch(String userId1, String userId2) {
        UserDetail userDetail1 = userMapper.getUserDetail(userId1);
        UserDetail userDetail2 = userMapper.getUserDetail(userId2);
        return calculateUserMatchByDetail(userDetail1, userDetail2);
    }

    @Override
    public Double calculateUserMatchByDetail(UserDetail userDetail1, UserDetail userDetail2) {
        if (userDetail1 == null || userDetail2 == null) {
            return 0.0;
        }
        Double match = 0.0;
        match += MatchSupporter.getConMatch(userDetail1.getCon(), userDetail2.getCon());
        match += MatchSupporter.getRoleMatch(userDetail1.getRole(), userDetail2.getRole());
        match += MatchSupporter.getFactorMatch(userDetail1.getCharacter(), userDetail2.getCharacter());

        if (!StringUtils.isEmpty(userDetail1.getNatureCharacter()) && !StringUtils.isEmpty(userDetail2.getNatureCharacter())) {
            log.info("userDetail1:"+userDetail1+" userDetail2:"+userDetail2);
            double cMatch1 = userMatchMapper.getCharacterMatch(userDetail1.getNatureCharacter(), userDetail2.getNatureCharacter());
            double cMatch2 = userMatchMapper.getCharacterMatch(userDetail2.getNatureCharacter(), userDetail1.getNatureCharacter());
            match += cMatch1 * cMatch2 * 0.001;
        }

        List<String> userGroups1 = userDetail1.getPreferGroup();
        if (CollectionUtils.isEmpty(userGroups1)) {
            userGroups1 = userMapper.getPreferGroup(userDetail1.getUserId());
        }
        List<String> userGroups2 = userDetail2.getPreferGroup();
        if (CollectionUtils.isEmpty(userGroups1)) {
            userGroups2 = userMapper.getPreferGroup(userDetail2.getUserId());
        }
        if (!CollectionUtils.isEmpty(userGroups1) && !CollectionUtils.isEmpty(userGroups2)) {
            double match1 = userMatchMapper.getGroupMatch("'" + String.join("','", userGroups1) + "'", userDetail2.getGroup());
            double match2 = userMatchMapper.getGroupMatch("'" + String.join("','", userGroups2) + "'", userDetail1.getGroup());
            match += match1 * match2 * 0.0045;
        }
        return match;
    }

    @Override
    public List<UserMatch> getUserMatchByPage(String userId, int start, int size) {
        return userMatchMapper.getMatchByPage(userId, start, size);
    }

    @Override
    public void updateMatch(UserMatch userMatch) {
        userMatchMapper.updateMatchCache(userMatch.getUserId1(), userMatch.getUserId2(), userMatch.getMatch());
    }

    @Override
    public Double getUserMatch(String userId1, String userId2) {
        String matchKey = CorgiConstants.getUserMatchKey(userId1, userId2);
        String matchStr = redisTemplate.opsForValue().get(matchKey);
        if (StringUtils.isEmpty(matchStr)) {
            Double match = userMatchMapper.getMatchCache(userId1, userId2);
            if (match == null) {
                match = this.calculateUserMatch(userId1, userId2);
                userMatchMapper.addMatchCache(userId1, userId2, match);
            }
            redisTemplate.opsForValue().set(matchKey, match.toString(), 90L, TimeUnit.DAYS);
            return match;
        }
        Double match = null;
        try {
            match = Double.valueOf(matchKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (match == null) {
            match = this.calculateUserMatch(userId1, userId2);
            redisTemplate.opsForValue().set(matchKey, String.valueOf(match), 90L, TimeUnit.DAYS);
        }
        return match;
    }
}
