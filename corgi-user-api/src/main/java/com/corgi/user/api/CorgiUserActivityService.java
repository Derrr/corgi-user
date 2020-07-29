package com.corgi.user.api;

import com.corgi.activity.entity.CorgiActivity;
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

    void deleteSignUpByActivity(String activityId);

    Integer getStatus(String userId, String activityId);

    List<UserProfile> getUsers(String activityId, String userId, String status);

    List<UserProfile> getPopularUsers(String activityId, String status);

    Integer countUsers(String activityId, String status);

    List<String> getSignUpActivity(String userId, Integer page, Integer pageSize);

    List<String> getHeatActivity(CorgiActivity corgiActivity, Integer page, Integer pageSize);

    void addActivityCreator(String userId, String activityId, String category);

    long countActivity(String date, String category);

    long countActivityUser(String date, String category);


    void deleteActivityCreator(String activityId);
}
