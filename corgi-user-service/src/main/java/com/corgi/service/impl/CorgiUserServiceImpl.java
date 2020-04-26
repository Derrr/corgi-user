package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.common.CorgiQueueName;
import com.corgi.common.messages.MatchRefresher;
import com.corgi.entity.ActivityQuery;
import com.corgi.entity.CorgiPic;
import com.corgi.mapper.*;
import com.corgi.support.UserQuerySupporter;
import com.corgi.user.api.CorgiUserFollowService;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.*;
import com.corgi.user.api.CorgiUserService;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserService.class)
@Slf4j
@Component
public class CorgiUserServiceImpl implements CorgiUserService {
    private static int MAX_PROFILE_SIZE = 30;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private CorgiUserMatchService corgiUserMatchService;
    @Autowired
    private CorgiUserFollowService corgiUserFollowService;
    @Autowired
    private CorgiPicMapper corgiPicMapper;
    @Autowired
    private CorgiUserTagMapper corgiUserTagMapper;
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;
    @Autowired
    private CorgiBlacklistMapper corgiBlacklistMapper;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserLogin login(UserLogin userLogin) {
        userLogin.setStatus("-1");
        UserLogin tmpUserLogin = corgiUserMapper.getUserLoginByTelNo(userLogin.getTelNo());
        if (tmpUserLogin != null && !StringUtils.isEmpty(tmpUserLogin.getUserId())) {
            userLogin.setUserId(tmpUserLogin.getUserId());
            if (!StringUtils.isEmpty(userLogin.getImId())) {
                corgiUserMapper.updateImId(userLogin);
            }
            userLogin.setStatus(String.valueOf(corgiUserMapper.countUserDetail(tmpUserLogin.getUserId())));
            return userLogin;
        }
        corgiUserMapper.addUserLogin(userLogin);
        return userLogin;
    }

    @Override
    public UserLogin getUserLogin(String userId) {
        return corgiUserMapper.getUserLogin(userId);
    }

    @Override
    public String addDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        if (corgiUserMapper.countUserDetail(userDetail.getUserId()) > 0) {
            return CorgiConstants.SUCCESS;
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserMapper.addUserDetail(userDetail);
        this.updatePreferGroup(userDetail.getUserId(), userDetail.getPreferGroup());
        if (userDetail.getUserPics() != null) {
            for (UserPic userPic : userDetail.getUserPics()) {
                corgiPicMapper.addUserPic(userPic);
            }
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String updateDetail(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getUserId())) {
            return "user id is null";
        }
        userDetail.setCon(UserUtils.getConByBirthDay(userDetail.getBirthday()));
        corgiUserMapper.updateUserDetail(userDetail);

        if (shouldRefresh(userDetail)) {
            MatchRefresher matchRefresher = new MatchRefresher();
            matchRefresher.setUserId(userDetail.getUserId());
            rabbitTemplate.convertAndSend(CorgiQueueName.REFRESH_MATCH_QUEUE, matchRefresher);
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public UserDetail getUserDetail(String userId, String loginUserId) {
        UserDetail userDetail = corgiUserMapper.getUserDetail(userId);
        if (userDetail == null) {
            return null;
        }
        List<UserPic> userPics = corgiPicMapper.getUserPic(userId);
        userDetail.setUserPics(userPics);
        if (!StringUtils.isEmpty(loginUserId)) {
            Integer countBeBlock = corgiBlacklistMapper.countBlack(userId, loginUserId);
            if (countBeBlock != null && countBeBlock > 0) {
                userDetail.setCheckStatus("block");
                return userDetail;
            }
            Integer countBlock = corgiBlacklistMapper.countBlack(loginUserId, userId);
            if (countBlock != null && countBlock > 0) {
                userDetail.setCheckStatus("blocked");
                return userDetail;
            }
        }
        List<String> groups = corgiUserMapper.getPreferGroup(userId);
        userDetail.setPreferGroup(groups);
        userDetail.setTags(corgiUserTagMapper.getUserTag(userId));
        userDetail.setInterests(corgiUserTagMapper.getUserInterests(userId));
        if (!StringUtils.isEmpty(loginUserId)) {
            userDetail.setMatch(corgiUserMatchService.getUserMatch(userId, loginUserId));
        }
        return userDetail;
    }

    @Override
    public String updatePreferGroup(String userId, List<String> groups) {
        if (StringUtils.isEmpty(userId)) {
            return "user id is null";
        }
        corgiUserMapper.deletePreferGroup(userId);
        if (!CollectionUtils.isEmpty(groups)) {
            for (String group : groups) {
                corgiUserMapper.addPreferGroup(userId, group);
            }
        }
        MatchRefresher matchRefresher = new MatchRefresher();
        matchRefresher.setUserId(userId);
        rabbitTemplate.convertAndSend(CorgiQueueName.REFRESH_MATCH_QUEUE, matchRefresher);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public long countPreferGroup(String group) {
        return corgiUserMapper.countPreferGroup(group);
    }

    @Override
    public long countBirthday(String beginDate, String endDate) {
        return corgiUserMapper.countBirthday(beginDate, endDate);
    }

    @Override
    public String updateUserPosition(UserPosition userPosition) {
        if (userPosition == null || StringUtils.isEmpty(userPosition.getUserId())) {
            return "user id is empty";
        }
        Long now = System.currentTimeMillis();
        String geoKey = "user";
        UserPosition oldUserPosition = corgiUserMapper.getUserPosition(userPosition.getUserId());
        if (oldUserPosition == null) {
            corgiUserMapper.addUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
            this.addGeo(geoKey, userPosition);
        } else if (oldUserPosition.getLat() - userPosition.getLat() > 0.0001
                || oldUserPosition.getLat() - userPosition.getLat() < -0.0001
                || oldUserPosition.getLng() - userPosition.getLng() > 0.0001
                || oldUserPosition.getLng() - userPosition.getLng() < -0.0001) {
            corgiUserMapper.updateUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
            redisTemplate.opsForGeo().remove(geoKey, userPosition.getUserId());
            this.addGeo(geoKey, userPosition);
        } else {
            corgiUserMapper.updateUserPositionUptime(userPosition.getUserId(), now);
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<UserPosition> getUserPositionByPage(Integer page, Integer pageSize) {
        return corgiUserMapper.getUserPositionByPage((page - 1) * pageSize, pageSize);
    }

    @Override
    public List<UserProfile> getNearByUser(UserQuery userQuery) {
        List<String> userIds = new ArrayList<>();
        if (hasFilter(userQuery)) {
            UserQuerySupporter supporter = new UserQuerySupporter(userQuery);
            userIds = corgiUserMapper.getNearByUser(supporter);
        } else {
            GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = redisTemplate.opsForGeo().radius("user", new Circle(new Point(userQuery.getLng(), userQuery.getLat()), new Distance(userQuery.getRange(), Metrics.KILOMETERS)));
            List<String> finalUserIds = userIds;
            geoResults.forEach(result -> finalUserIds.add(result.getContent().getName()));
        }
        String inValue = getUserSql(userIds, userQuery.getUserId());
        if (StringUtils.isEmpty(inValue)) {
            return new ArrayList<>();
        }
        List<UserProfile> userProfiles = corgiUserMapper.getUserProfileList(inValue);
        String userId1 = userQuery.getUserId();
        userProfiles = this.populateUserProfileAll(userProfiles, userId1, true);
        return userProfiles;
    }

    private boolean hasFilter(UserQuery userQuery) {
        return !StringUtils.isEmpty(userQuery.getNickname())
                || !CollectionUtils.isEmpty(userQuery.getGroup())
                || !CollectionUtils.isEmpty(userQuery.getRole())
                || (userQuery.getEndWeight() != null && userQuery.getEndWeight() < 200)
                || (userQuery.getStartWeight() != null && userQuery.getStartWeight() > 30)
                || (userQuery.getEndHeight() != null && userQuery.getEndHeight() < 200)
                || (userQuery.getStartHeight() != null && userQuery.getStartWeight() > 30)
                || !CollectionUtils.isEmpty(userQuery.getRelation());
    }

    @Override
    public List<String> getAllNearByUser(UserQuery userQuery) {
        UserQuerySupporter supporter = new UserQuerySupporter(userQuery);
        List<String> userIds = corgiUserMapper.getNearByUser(supporter);
        return userIds;
    }

    @Override
    public List<UserProfile> searchUsers(UserDetail userDetail, String userId, Integer page, Integer pageSize) {
        List<UserProfile> userProfiles = corgiUserMapper.queryUserProfile(userDetail, page < 1 ? 0 : (page - 1) * pageSize, pageSize);
        return populateUserProfile(userProfiles, userId);
    }

    @Override
    public long countUsers(UserDetail userDetail) {
        return corgiUserMapper.countUserProfile(userDetail);
    }

    @Override
    public long countActiveUser(long beginTime, long endTime) {
        return corgiUserMapper.countActiveUser(beginTime, endTime);
    }

    @Override
    public long countUserStay(long time, String registerDate) {
        Long result = corgiUserMapper.countUserStay(time, registerDate);
        if (result == null) {
            return 0L;
        }
        return result;
    }


    @Override
    public long countRegisterUser(String date) {
        return corgiUserMapper.countRegisterUser(date);
    }

    @Override
    public List<String> filterUser(List<String> userIds, ActivityQuery activityQuery) {
        if (CollectionUtils.isEmpty(userIds)) {
            return userIds;
        }
        StringBuilder sb = new StringBuilder("(");
        for (String userId : userIds) {
            sb.append("'").append(userId).append("',");
        }
        sb.deleteCharAt(sb.length() - 1).append(")");
        return corgiUserMapper.filterUser(sb.toString(),
                activityQuery.getRoleStr(),
                activityQuery.getGroupStr(),
                activityQuery.getPreferGroupStr(),
                activityQuery.getUserId());
    }

    @Override
    public List<UserProfile> populateUserProfile(List<UserProfile> userProfiles, String userId) {
        userProfiles = populateUserProfileAll(userProfiles, userId, true);
        return userProfiles;
    }

    public List<UserProfile> populateUserProfileAll(List<UserProfile> userProfiles, String userId, boolean hasMatch) {
        UserDetail loginUserDetail = null;
        if (!CollectionUtils.isEmpty(userProfiles)) {
            for (UserProfile userProfile : userProfiles) {
                try {
                    List<UserPic> userPics = corgiPicMapper.getUserPic(userProfile.getUserId());
                    userProfile.setPics(userPics);
                    if (StringUtils.isEmpty(userId)) {
                        continue;
                    }
                    String userId2 = userProfile.getUserId();
                    int count = corgiUserFollowService.isFollowed(userId, userId2);
                    userProfile.setIsFollowed(count);
                    if (hasMatch) {
                        Double match = corgiUserMatchService.getUserMatch(userId, userId2);
                        if (match == null) {
                            if (loginUserDetail == null) {
                                loginUserDetail = corgiUserMapper.getUserDetail(userId);
                                if (loginUserDetail == null) {
                                    continue;
                                }
                                loginUserDetail.setPreferGroup(corgiUserMapper.getPreferGroup(userId));
                            }
                            UserDetail userDetail = corgiUserMapper.getUserDetail(userProfile.getUserId());
                            userDetail.setPreferGroup(corgiUserMapper.getPreferGroup(userDetail.getUserId()));
                            try {
                                match = corgiUserMatchService.calculateUserMatchByDetail(loginUserDetail, userDetail);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                        userProfile.setMatch(match);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return userProfiles;
    }

    @Override
    public String updateUserLogin(UserLogin userLogin) {
        corgiUserMapper.updateLogin(userLogin);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public void updatePush(UserLogin userLogin) {
        corgiUserMapper.updatePush(userLogin);
    }

    @Override
    public String updateUserNickname(String userId, String nickname, String checkNickname) {
        UserDetail detail = corgiUserMapper.getUserDetail(userId);
        if ("fail".equals(detail.getCheckStatus())) {
            corgiUserMapper.updateNickname(userId, nickname, detail.getCheckNickname());
        } else {
            int count = corgiUserMapper.countNickname(nickname, checkNickname, userId);
            if (count > 0) {
                return "nickname exists";
            }
            corgiUserMapper.updateNickname(userId, nickname, checkNickname);
        }
        return CorgiConstants.SUCCESS;
    }

    @Override
    public int countUserNickname(String nickname) {
        return corgiUserMapper.countNickname(nickname, "", "");
    }

    @Override
    public void deleteUser(String userId) {
        corgiUserMapper.deleteUserLogin(userId);
        corgiUserMapper.deleteUserDetail(userId);
        corgiUserMapper.deleteUserPosition(userId);
        corgiUserMapper.deletePreferGroup(userId);
        corgiUserFollowMapper.deleteAllUserFollow(userId);
        corgiBlacklistMapper.deleteAll(userId);
        redisTemplate.opsForGeo().remove("user", userId);
    }

    private void addGeo(String geoKey, UserPosition userPosition) {
        if (userPosition.getLng() == null || userPosition.getLng() > 180 || userPosition.getLng() < -180) {
            return;
        }
        if (userPosition.getLat() == null || userPosition.getLat() > 90 || userPosition.getLat() < -90) {
            return;
        }
        if (StringUtils.isEmpty(userPosition.getUserId()) || StringUtils.isEmpty(geoKey)) {
            return;
        }
        redisTemplate.opsForGeo().add(geoKey, new Point(userPosition.getLng(), userPosition.getLat()), userPosition.getUserId());
    }

    private String getUserSql(List<String> userIds, String loginUserId) {
        //若没有人则返回空
        if (CollectionUtils.isEmpty(userIds)) {
            return "";
        }
        int size = userIds.size();
        if (size > MAX_PROFILE_SIZE * 2) {
            Random r = new Random();
            List<String> tmpUserIds = new ArrayList<>();
            for (int i = 0; i < MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size());
                tmpUserIds.add(userIds.remove(index));
            }
            userIds = tmpUserIds;
        } else if (size > MAX_PROFILE_SIZE) {
            //若人数不多，则剔除多余人
            Random r = new Random();
            for (int i = 0; i < size - MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size());
                userIds.remove(index);
            }

        }

        StringBuilder sb = new StringBuilder("('");
        for (String userId : userIds) {
            if (!userId.equals(loginUserId)) {
                sb.append(userId + "','");
            }
        }
        if (sb.length() > 2) {
            return sb.delete(sb.length() - 2, sb.length()).append(")").toString();
        } else {
            return "";
        }
    }

    private boolean shouldRefresh(UserDetail userDetail) {
        if (userDetail.getWeight() > 0) {
            return true;
        }
        if (userDetail.getHeight() > 0) {
            return true;
        }
        if (!StringUtils.isEmpty(userDetail.getGroup())) {
            return true;
        }
        if (!StringUtils.isEmpty(userDetail.getCharacter())) {
            return true;
        }
        if (!StringUtils.isEmpty(userDetail.getRole())) {
            return true;
        }
        if (!StringUtils.isEmpty(userDetail.getBirthday())) {
            return true;
        }
        return false;
    }

}
