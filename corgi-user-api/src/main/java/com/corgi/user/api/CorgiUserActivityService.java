package com.corgi.user.api;

import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserActivityService {
    boolean signUp(UserSignUp userSignUp);

    boolean signOut(UserSignUp userSignUp);

    boolean updateSignUp(UserSignUp userSignUp);

    void deleteActivity(String activityId);

    Integer getStatus(String userId, String activityId);

    List<UserProfile> getUsers(String activityId, String userId);

    List<String> getSignUpActivity(String userId, Integer page, Integer pageSize);
}
