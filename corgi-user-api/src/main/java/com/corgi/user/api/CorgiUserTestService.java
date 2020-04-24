package com.corgi.user.api;

import com.corgi.entity.ActivityQuery;
import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserTestService {
    UserLogin login(UserLogin userLogin);

    UserLogin getUserLogin(String userId);

    String addDetail(UserDetail userDetail);

    String updateDetail(UserDetail userDetail);

    UserDetail getUserDetail(String userId, String loginUserId);

    long countBirthday(String beginDate, String endDate);

    List<UserProfile> searchUsers(UserDetail userDetail, String userId, Integer page, Integer pageSize);

    long countUsers(UserDetail userDetail);

    String updateUserLogin(UserLogin userLogin);

    void updatePush(UserLogin userLogin);

    String updateUserNickname(String userId, String nickname, String checkNickname);

    int countUserNickname(String nickname);

    void deleteUser(String userId);
}
