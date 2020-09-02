package com.corgi.user.api;

import com.corgi.entity.ActivityQuery;
import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserService {
    UserLogin login(UserLogin userLogin);

    UserLogin getUserLogin(String userId);

    String addDetail(UserDetail userDetail);

    String updateDetail(UserDetail userDetail);

    UserDetail getUserDetail(String userId, String loginUserId);

    String updatePreferGroup(String userId, List<String> groups);

    long countPreferGroup(String group);

    long countBirthday(String beginDate, String endDate);

    String updateUserPosition(UserPosition userPosition);

    UserPosition getUserPosition(String userId);

    List<UserPosition> getUserPositionByPage(Integer page, Integer pageSize);

    List<UserProfile> getNearByUser(UserQuery userQuery);

    List<String> getAllNearByUser(UserQuery userQuery);

    List<UserProfile> getAllNearByUserProfile(UserQuery userQuery);

    List<UserProfile> searchUsers(UserDetail userDetail, String userId, Integer page, Integer pageSize);

    long countUsers(UserDetail userDetail);

    long countActiveUser(long beginTime, long endTime);

    long countUserStay(long time, String registerDate);

    long countRegisterUser(String date);

    List<String> filterUser(List<String> userIds, ActivityQuery activityQuery);

    List<UserProfile> populateUserProfile(List<UserProfile> userProfiles, String userId);

    String updateUserLogin(UserLogin userLogin);

    void updatePush(UserLogin userLogin);

    String updateUserNickname(String userId, String nickname, String checkNickname);

    int countUserNickname(String nickname);

    void deleteUser(String userId);
}
