package com.corgi.user.api;

import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserActivityService {
    boolean signUp(UserSignUp userSignUp);

    boolean updateSignUp(UserSignUp userSignUp);

    List<UserProfile> getUsers(String activityId, String userId);
}
