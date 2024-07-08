package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.corgi.activity.api.CorgiMatchService;
import com.corgi.common.CorgiConstants;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiUserMatchMapper;
import com.corgi.support.MatchSupporter;
import com.corgi.support.UserQuerySupporter;
import com.corgi.user.api.CorgiOrderService;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.*;
import com.corgi.user.enums.MerchandiseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
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
    private static final String REMAIN = "剩余:%d次";
    @Autowired
    private CorgiUserMatchMapper userMatchMapper;
    @Autowired
    private CorgiUserMapper userMapper;
    @Reference
    private CorgiOrderService corgiOrderService;
    @Reference
    private CorgiMatchService corgiMatchService;
    @Reference
    private CorgiUserService corgiUserService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<UserMatchItem> getUserMatchItem(UserQuery userQuery) {
        Calendar calendar = Calendar.getInstance();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        this.refreshMatchQuery(userQuery);
        UserDetail userDetail = corgiUserService.getUserDetailBasic(userQuery.getUserId());
        UserExtra userExtra = userMapper.getUserExtra(userQuery.getUserId());
        String key = "user_match_view_" + dateStr + userQuery.getUserId();
        List<UserMatchItem> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<UserMatchItem> users = corgiMatchService.getMatchItems(userQuery);
            log.info("query user item size:{}", users.size());
            if (CollectionUtils.isEmpty(users)) {
                break;
            }
            List<String> userIds = new ArrayList<>();
            for (UserMatchItem item : users) {
                if (this.checkMatchItem(item, userDetail, userExtra)) {
                    userIds.add(item.getUserId());
                    result.add(item);
                }else{
                    log.info("query user not match");
                    userIds.add(item.getUserId());
                }
                if (result.size() >= 6) {
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(userIds)) {
                try {
                    if (redisTemplate.hasKey(key)) {
                        redisTemplate.opsForList().rightPushAll(key, userIds);
                    } else {
                        redisTemplate.opsForList().rightPushAll(key, userIds);
                        redisTemplate.expire(key, 1l, TimeUnit.DAYS);
                    }
                } catch (Exception e) {
                    redisTemplate.delete(key);
                }
            }
            if (result.size() >= 6) {
                break;
            }
        }
        return result;
    }

    @Override
    public Integer countAllMatcher(UserQuery userQuery) {
        UserQuerySupporter supporter = new UserQuerySupporter(userQuery);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        return userMatchMapper.countMatchByRange(supporter, calendar.getTimeInMillis());
    }

    @Override
    public void clearMatchByDate(String date) {
        userMatchMapper.updateMatchByDate(date);
    }

    @Override
    public void clearMatchViewByDate(String date) {
        userMatchMapper.updateMatchViewByDate(date, null);
    }

    @Override
    public List<UserMatchRemain> countUserRemain(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        List<UserMatchRemain> remains = new ArrayList<>();
        UserMatchRemain remain0 = new UserMatchRemain();
        Integer free = 600;
        remain0.setUserId(userId);
        remain0.setTradeNo("0");
        remain0.setRemain(free - userMatchMapper.countMatch(userId, "0", sdf.format(calendar.getTime())));
        remains.add(remain0);
        return remains;
    }

    @Override
    public void addUserMatch(String userId, String matchId, String tradeNo) {
        String key = "user_match_" + userId;
        try {
            if (redisTemplate.hasKey(key)) {
                redisTemplate.opsForList().rightPush(key, matchId + "-" + System.currentTimeMillis());
            } else {
                redisTemplate.opsForList().rightPush(key, matchId + "-" + System.currentTimeMillis());
                redisTemplate.expire(key, 14l, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            redisTemplate.delete(key);
        }
    }

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
        Double match = userMatchMapper.getMatchCache(userId1, userId2);
        if (match == null) {
            match = this.calculateUserMatch(userId1, userId2);
            userMatchMapper.addMatchCache(userId1, userId2, match);
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

    @Override
    public UserQuery getUserQuery(String userId) {
        MatchQuery matchQuery = userMatchMapper.getUserMatchQuery(userId);
        if (matchQuery == null || "0".equals(matchQuery.getStatus())) {
            return null;
        }
        try {
            return JSONObject.parseObject(matchQuery.getQuery(), UserQuery.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void refreshMatchQuery(UserQuery userQuery) {
        try {
            MatchQuery matchQuery = userMatchMapper.getUserMatchQuery(userQuery.getUserId());
            if (userQuery.getStartAge() == null && matchQuery != null && "1".equals(matchQuery.getStatus())) {
                userMatchMapper.updateMatchQuery(userQuery.getUserId(), "", "0");
            }
            if (userQuery.getStartAge() != null) {
                String query = JSONObject.toJSONString(userQuery);
                if (matchQuery == null) {
                    userMatchMapper.addMatchQuery(userQuery.getUserId(), query);
                } else if ("0".equals(matchQuery.getStatus()) || !query.equals(matchQuery.getQuery())) {
                    userMatchMapper.updateMatchQuery(userQuery.getUserId(), query, "1");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean checkMatchItem(UserMatchItem item, UserDetail userDetail, UserExtra userExtra) {
        UserQuery query = item.getQuery();
        if (query == null) {
            return true;
        }
        if (query.getRange() != null) {
            String distance = item.getDistance().replaceAll("km", "");
            Double range = query.getRange();
            if (distance.startsWith(">")) {
                if (range < 100) {
                    return false;
                }
            } else {
                try {
                    Integer dis = Integer.valueOf(distance);
                    if (range < dis) {
                        return false;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        if ("verify".equals(query.getType())
                && !"verified".equals(userDetail.getAvatarCheckStatus())) {
            return false;
        }
        if (query.getStartAge() != null && query.getEndAge() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1 * query.getStartAge());
            String endYear = sdf.format(calendar.getTime());
            calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1 * query.getEndAge());
            String startYear = sdf.format(calendar.getTime());
            String birthDay = userDetail.getBirthday();
            if (!StringUtils.isEmpty(birthDay) &&
                    (startYear.compareTo(userDetail.getBirthday()) > 0
                            || endYear.compareTo(userDetail.getBirthday()) < 0)) {
                return false;
            }
        }
        if (query.getStartHeight() != null && query.getEndHeight() != null) {
            int height = userDetail.getHeight();
            if (height < query.getStartHeight() || height > query.getEndHeight()) {
                return false;
            }
        }
        if (query.getStartWeight() != null && query.getEndWeight() != null) {
            int weight = userDetail.getWeight();
            if (weight < query.getStartWeight() || weight > query.getEndWeight()) {
                return false;
            }
        }
        if (!CollectionUtils.isEmpty(query.getRole())
                && !query.getRole().contains(userDetail.getRole())) {
            return false;
        }
        if (!CollectionUtils.isEmpty(query.getGroup())
                && !query.getGroup().contains(userDetail.getHideGroup())) {
            return false;
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getAim())
                && !StringUtils.isEmpty(userExtra.getAim())
                && !query.getAim().contains(userExtra.getAim())) {
            return false;
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getEducation())
                && !StringUtils.isEmpty(userExtra.getEducation())
                && !query.getEducation().contains(userExtra.getEducation())) {
            return false;
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getIncome())
                && !StringUtils.isEmpty(userExtra.getIncome())
                && !query.getIncome().contains(userExtra.getIncome())) {
            return false;
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getProfession())
                && !StringUtils.isEmpty(userExtra.getProfession())
                && !query.getProfession().contains(userExtra.getProfession())) {
            return false;
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getInterests())
                && !StringUtils.isEmpty(userExtra.getInterests())) {
            List<String> interests = Arrays.asList(userExtra.getInterests().split(","));
            query.getInterests().retainAll(interests);
            if (query.getInterests().size() == 0) {
                return false;
            }
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getXp())
                && !StringUtils.isEmpty(userExtra.getXp())) {
            List<String> xps = Arrays.asList(userExtra.getXp().split(","));
            query.getXp().retainAll(xps);
            if (query.getXp().size() == 0) {
                return false;
            }
        }
        if (userExtra != null && !CollectionUtils.isEmpty(query.getTags())
                && !StringUtils.isEmpty(userExtra.getTags())) {
            List<String> tags = Arrays.asList(userExtra.getTags().split(","));
            query.getTags().retainAll(tags);
            if (query.getTags().size() == 0) {
                return false;
            }
        }

        return true;
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

    private List<UserMatchItem> buildUsers(List<UserMatchItem> items, List<String> userIds, Long nowTime, String nowTimeDate, int size) {
        List<UserMatchItem> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(items)) {
            return result;
        }
        for (UserMatchItem item : items) {
            if (size <= 0) {
                return result;
            }
            if (userIds.contains(item.getUserId())) {
                continue;
            }
            userIds.add(item.getUserId());
            if (StringUtils.isEmpty(item.getDistance())) {
                item.setDistance("");
            } else {
                String distance = "0km";
                try {
                    Integer dis = Integer.valueOf(item.getDistance().split("\\.")[0]);
                    if (dis > 100) {
                        distance = ">100km";
                    } else {
                        distance = dis + "km";
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                item.setDistance(distance);
            }
            if (StringUtils.isEmpty(item.getDateStatus())) {
                item.setDateStatus("想聊天");
            }
            if (StringUtils.isEmpty(item.getAvatarStatus()) || "-".equals(item.getAvatarStatus())) {
                item.setAvatarStatus("");
            } else if ("influencer".equals(item.getAvatarStatus())) {
                item.setAvatarStatus("influencer");
            } else if (nowTimeDate.compareTo(item.getAvatarStatus()) <= 0) {
                item.setAvatarStatus("vip");
            } else {
                item.setAvatarStatus("");
            }
            try {
                Long timestamp = Long.valueOf(item.getTimeShow());
                Long diff = nowTime - timestamp;
                if (diff < 5 * 60 * 1000) {
                    item.setTimeShow("在线");
                } else if (diff < 2 * 3600 * 1000) {
                    item.setTimeShow("刚刚");
                } else if (diff < 3 * 24 * 3600 * 1000) {
                    item.setTimeShow("今日");
                } else {
                    item.setTimeShow("本周");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                item.setTimeShow("本周");
            }
            result.add(item);
            size--;
        }
        return result;
    }

}
