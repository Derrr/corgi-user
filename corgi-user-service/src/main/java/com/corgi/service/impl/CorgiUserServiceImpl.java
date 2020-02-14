package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.common.CorgiConstants;
import com.corgi.common.CorgiQueueName;
import com.corgi.common.messages.MatchRefresher;
import com.corgi.entity.ActivityQuery;
import com.corgi.mapper.CorgiPicMapper;
import com.corgi.mapper.CorgiUserFollowMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiUserTagMapper;
import com.corgi.support.UserQuerySupporter;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.*;
import com.corgi.user.api.CorgiUserService;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private static int MAX_PROFILE_SIZE = Integer.MAX_VALUE;
    @Autowired
    private CorgiUserMapper corgiUserMapper;
    @Autowired
    private CorgiUserMatchService corgiUserMatchService;
    @Autowired
    private CorgiPicMapper corgiPicMapper;
    @Autowired
    private CorgiUserTagMapper corgiUserTagMapper;
    @Autowired
    private CorgiUserFollowMapper corgiUserFollowMapper;
    @Autowired
    private AmqpTemplate rabbitTemplate;

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
            return "user exists";
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

        MatchRefresher matchRefresher = new MatchRefresher();
        matchRefresher.setUserId(userDetail.getUserId());
        rabbitTemplate.convertAndSend(CorgiQueueName.REFRESH_MATCH_QUEUE, matchRefresher);

        return CorgiConstants.SUCCESS;
    }

    @Override
    public UserDetail getUserDetail(String userId) {
        UserDetail userDetail = corgiUserMapper.getUserDetail(userId);
        if (userDetail == null) {
            return null;
        }
        List<String> groups = corgiUserMapper.getPreferGroup(userId);
        List<UserPic> userPics = corgiPicMapper.getUserPic(userId);

        userDetail.setPreferGroup(groups);
        userDetail.setUserPics(userPics);

        userDetail.setTags(corgiUserTagMapper.getUserTag(userId));
        userDetail.setInterests(corgiUserTagMapper.getUserInterests(userId));
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
        UserPosition oldUserPosition = corgiUserMapper.getUserPosition(userPosition.getUserId());
        if (oldUserPosition == null) {
            corgiUserMapper.addUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
        } else if (!oldUserPosition.getLat().equals(userPosition.getLat()) || (!oldUserPosition.getLng().equals(userPosition.getLng()))) {
            corgiUserMapper.updateUserPosition(userPosition.getUserId(), userPosition.getLat(), userPosition.getLng(), now);
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
        UserQuerySupporter supporter = new UserQuerySupporter(userQuery);
        List<String> userIds = corgiUserMapper.getNearByUser(supporter);
        String inValue = getUserSql(userIds, userQuery.getUserId());
        if (StringUtils.isEmpty(inValue)) {
            return new ArrayList<>();
        }
        List<UserProfile> userProfiles = corgiUserMapper.getUserProfileList(inValue);
        String userId1 = userQuery.getUserId();
        userProfiles = this.populateUserProfile(userProfiles, userId1);
        return userProfiles;
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
        return corgiUserMapper.countUserStay(time, registerDate);
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
                activityQuery.getPreferGroupStr());
    }

    @Override
    public List<UserProfile> populateUserProfile(List<UserProfile> userProfiles, String userId) {
        UserDetail loginUserDetail = null;
        if (!CollectionUtils.isEmpty(userProfiles)) {
            for (UserProfile userProfile : userProfiles) {
                userProfile.setPics(corgiPicMapper.getUserPic(userProfile.getUserId()));
                if (StringUtils.isEmpty(userId)) {
                    continue;
                }
                String userId2 = userProfile.getUserId();
                int count = corgiUserFollowMapper.countFollow(userId2, userId);
                userProfile.setIsFollowed(count);
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
        }
        return userProfiles;
    }

    @Override
    public String updateUserLogin(UserLogin userLogin) {
        corgiUserMapper.updateLogin(userLogin);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String updateUserNickname(String userId, String nickname, String checkNickname) {
        int count = corgiUserMapper.countNickname(nickname, checkNickname);
        if (count > 0) {
            return "nickname exists";
        }
        corgiUserMapper.updateNickname(userId, nickname, checkNickname);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public int countUserNickname(String nickname) {
        return corgiUserMapper.countNickname(nickname, "");
    }

    @Override
    public void deleteUser(String userId) {
        corgiUserMapper.deleteUserLogin(userId);
        corgiUserMapper.deleteUserDetail(userId);
        corgiUserMapper.deleteUserPosition(userId);
        corgiUserMapper.deletePreferGroup(userId);
        corgiUserFollowMapper.deleteAllUserFollow(userId);
    }

    private String getUserSql(List<String> userIds, String loginUserId) {
        //若没有人则返回空
        if (CollectionUtils.isEmpty(userIds)) {
            return "";
        }


        /*if (userIds.size() > MAX_PROFILE_SIZE * 2) {
            //若人数很多则随机取16人
            Random r = new Random();
            List<String> tmpUserIds = new ArrayList<>();
            for (int i = 0; i < MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size() - i);
                tmpUserIds.add(userIds.remove(index));
            }
            userIds = tmpUserIds;
        } else if (userIds.size() > MAX_PROFILE_SIZE) {
            //若人数不多，则剔除多余人
            Random r = new Random();
            for (int i = 0; i < userIds.size() - MAX_PROFILE_SIZE; i++) {
                int index = r.nextInt(userIds.size() - i);
                userIds.remove(index);
            }

        }*/

        StringBuilder sb = new StringBuilder("('");
        for (String userId : userIds) {
            if (!userId.equals(loginUserId)) {
                sb.append(userId + "','");
            }
        }

        return sb.delete(sb.length() - 2, sb.length()).append(")").toString();
    }


}
