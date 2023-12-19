package com.corgi.user.api;

import com.corgi.user.entity.UserProfile;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserRecommendService {
    void clearRecUser(String userId);

    void followRecUser(String userId, String recId);

    void addRecUser(String userId, String recId, Double weight);

    void updateRecStatus(String userId, String recId, String status);

    List<UserProfile> getRecUser(String userId, Integer size);

    List<UserProfile> getVlogRecUser(String userId, Integer size);

    List<UserProfile> getInfluencerByCity(String userId, String city, Integer size);

    List<UserProfile> getCityPopulate(String userId, String city, Integer page, Integer size);

    void deleteRecUserByWeight(String userId, Integer weight);

    void distLikeUser(String userId, String disLikeUserId);

    void initInfluencer();

    List<String> getCityRecommendImage(String userId, String city, Integer page, Integer pageSize);

    List<String> getNotCityRecommendImage(String userId, String city, Integer page, Integer pageSize);

    void clearRecActivity(String userId);

    void addRecActivity(String userId, String recId, Double weight);

    void addGroupCor(String userId, String group);

    void updateGroupCor(String userId, String group, Double weight);

    HashMap<String,Double> getGroupCor(String userId);

    void addPreferCor(String userId, String group);

    void updatePreferCor(String userId, String group, Double weight);

    HashMap<String,Double> getPreferCor(String userId);

    void clearPreferCor(String param);

    Double getGroupWeight(Integer limit, String group);
}
