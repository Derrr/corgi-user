package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserService {
    UserLogin login(UserLogin userLogin);

    String addDetail(UserDetail userDetail);

    String updateDetail(UserDetail userDetail);

    UserDetail getUserDetail(String userId);

    String updatePreferGroup(String userId, List<String> groups);

    String updateUserPosition(UserPosition userPosition);

    List<UserProfile> getNearByUser(UserPosition userPosition, Double range);

    List<UserProfile> searchUsers(UserDetail userDetail);

    long countActiveUser(long beginTime, long endTime);

    long countRegisterUser(String date);
}
