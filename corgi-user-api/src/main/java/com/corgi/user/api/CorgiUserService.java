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

    UserDetail getUserDetail(String userId);

    String updatePreferGroup(String userId, List<String> groups);

    String updateUserPosition(UserPosition userPosition);

    List<UserProfile> getNearByUser(UserQuery userQuery);

    List<String> getAllNearByUser(UserQuery userQuery);

    List<UserProfile> searchUsers(UserDetail userDetail, Integer page, Integer pageSize);

    long countUsers(UserDetail userDetail);

    long countActiveUser(long beginTime, long endTime);

    long countRegisterUser(String date);

    List<String> filterUser(List<String> userIds, ActivityQuery activityQuery);

    List<UserProfile> populateUserProfile(List<UserProfile> userProfiles, String userId);

    String updateUserLogin(UserLogin userLogin);

    String updateUserNickname(String userId, String nickname, String checkNickname);

    void deleteUser(String userId);
}
