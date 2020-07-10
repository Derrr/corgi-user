package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiUserMatchMapper;
import com.corgi.support.MatchSupporter;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserMatch;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author tairanliu
 */
@Slf4j
@Service(interfaceClass = CorgiUserMatchService.class)
@Component
public class CorgiUserMatchServiceImpl implements CorgiUserMatchService {
    private static final String UNKNOWN = "未知";
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
        log.info("beginning match userId1:{}, userId2:{}", userDetail1.getUserId(), userDetail2.getUserId());
        Double match = 0.0;
        try {
            Map factors = redisTemplate.opsForHash().entries(CorgiConstants.MATCH_FACTOR);
            Double conFactor = getRatios("con_factor", factors);
            Double roleFactor = getRatios("role_factor", factors);
            Double charaFactor = getRatios("chara_factor", factors);
            Double preferFactor = getRatios("prefer_factor", factors);
            Double intFactor = getRatios("int_factor", factors);
            List<String> intList = redisTemplate.opsForList().range("match_factor_int", 0, -1);
            List<String> conList = redisTemplate.opsForList().range("match_factor_con", 0, -1);
            if (conFactor != null) {
                MatchSupporter.CON_FACTOR = conFactor / 100;
            }
            if (roleFactor != null) {
                MatchSupporter.ROLE_FACTOR = roleFactor / 100;
            }
            if (charaFactor != null) {
                MatchSupporter.CHARA_FACTOR = charaFactor / 100;
            }
            if (preferFactor != null) {
                MatchSupporter.PREFER_FACTOR = preferFactor / 100;
            }
            if (intFactor != null) {
                MatchSupporter.FACTOR = intFactor / 100;
            }
            if (intList != null && intList.size() == 2) {
                MatchSupporter.INT_MATCH_LIST = intList;
            }
            if (conList != null && conList.size() == 12) {
                MatchSupporter.CON_MATCH_LIST = conList;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        Double cronMatch = MatchSupporter.getConMatch(userDetail1.getCon(), userDetail2.getCon());
        log.info("con match:{}", cronMatch);
        match += cronMatch;

        Double factorMatch = MatchSupporter.getFactorMatch(userDetail1.getCharacter(), userDetail2.getCharacter());
        log.info("factor match:{}", factorMatch);
        match += factorMatch;

        if (!StringUtils.isEmpty(userDetail1.getRole()) && !StringUtils.isEmpty(userDetail2.getRole())) {
            Integer cMatch = userMatchMapper.getRoleMatch(userDetail1.getRole(), userDetail2.getRole());
            log.info("role match:{}", cMatch);
            if (cMatch != null) {
                match += cMatch * MatchSupporter.ROLE_FACTOR;
            }
        }
        if (!StringUtils.isEmpty(userDetail1.getNatureCharacter()) && !StringUtils.isEmpty(userDetail2.getNatureCharacter())) {
            Integer cMatch1 = userMatchMapper.getCharacterMatch(userDetail1.getNatureCharacter(), userDetail2.getNatureCharacter());
            log.info("character match1:{}", cMatch1);
            Integer cMatch2 = userMatchMapper.getCharacterMatch(userDetail2.getNatureCharacter(), userDetail1.getNatureCharacter());
            log.info("character match2:{}", cMatch2);
            if (cMatch1 != null && cMatch2 != null) {
                log.info("final character match:{}", Math.sqrt(cMatch1 * cMatch2));
                match += Math.sqrt(cMatch1 * cMatch2) * MatchSupporter.CHARA_FACTOR;
            }
        }

        List<String> userGroups1 = userDetail1.getPreferGroup();
        if (CollectionUtils.isEmpty(userGroups1)) {
            userGroups1 = userMapper.getPreferGroup(userDetail1.getUserId());
        }
        List<String> userGroups2 = userDetail2.getPreferGroup();
        if (CollectionUtils.isEmpty(userGroups2)) {
            userGroups2 = userMapper.getPreferGroup(userDetail2.getUserId());
        }
        Integer pMatch1 = 0;
        if (!CollectionUtils.isEmpty(userGroups1)) {
            pMatch1 = userMatchMapper.getGroupMatch("'" + String.join("','", userGroups1) + "'", userDetail2.getGroup());
            log.info("prefer match1:{}", pMatch1);
            if (pMatch1 == null) {
                pMatch1 = 0;
            }
        }
        Integer pMatch2 = 0;
        if (!CollectionUtils.isEmpty(userGroups2)) {
            pMatch2 = userMatchMapper.getGroupMatch("'" + String.join("','", userGroups2) + "'", userDetail1.getGroup());
            log.info("prefer match2:{}", pMatch2);
            if (pMatch2 == null) {
                pMatch2 = 0;
            }
        }
        log.info("final prefer match:{}", Math.sqrt(pMatch1 * pMatch2));
        match += Math.sqrt(pMatch1 * pMatch2) * MatchSupporter.PREFER_FACTOR;
        return Math.round(match) + 0.0;
    }

    @Override
    public List<UserMatch> getUserMatchByPage(String userId, int start, int size) {
        return userMatchMapper.getMatchByPage(userId, start, size);
    }

    @Override
    public void updateMatch(UserMatch userMatch) {
        if (userMatch.getMatch() <= 0) {
            userMatchMapper.deleteMatchCache(userMatch.getUserId1(), userMatch.getUserId2());
        }
        userMatchMapper.updateMatchCache(userMatch.getUserId1(), userMatch.getUserId2(), userMatch.getMatch());
    }

    @Override
    public Double getUserMatch(String userId1, String userId2) {
        String matchKey = CorgiConstants.getUserMatchKey(userId1, userId2);
        String matchStr = redisTemplate.opsForValue().get(matchKey);
        log.info("match key:{}, value:{}", matchKey, matchStr + "");
        if (StringUtils.isEmpty(matchStr)) {
            Double match = userMatchMapper.getMatchCache(userId1, userId2);
            if (match == null) {
                match = this.calculateUserMatch(userId1, userId2);
                userMatchMapper.addMatchCache(userId1, userId2, match);
            }
            redisTemplate.opsForValue().set(matchKey, match.toString(), 7L, TimeUnit.DAYS);
            return match;
        }
        Double match = null;
        try {
            match = Double.valueOf(matchStr);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (match == null) {
            match = this.calculateUserMatch(userId1, userId2);
            if (match != null) {
                redisTemplate.delete(matchKey);
                redisTemplate.opsForValue().set(matchKey, match.toString(), 7L, TimeUnit.DAYS);
            }
        }
        return match;
    }

    @Override
    public void clearMatch() {
        Set<String> keys = redisTemplate.keys(CorgiConstants.MATCH_PREFIX + "*");
        for (String key : keys) {
            log.info("clearing key:{}", key);
            redisTemplate.delete(key);
        }
    }

    @Override
    public List<HashMap> getMatchFactor(String table) {
        return userMatchMapper.getMatchFactor(table);
    }

    @Override
    public List<HashMap> updateMatchFactor(String table, String cn1, String cv1, String cn2, String cv2, Integer match) {
        userMatchMapper.updateMatchFactor(table, cn1, cv1, cn2, cv2, match);
        return null;
    }

    private Double getRatios(String key, Map factors) {

        String value = factors.get(key) + "";
        try {
            Double result = Double.valueOf(value);
            return result;
        } catch (Exception e) {

        }
        return null;
    }

}
