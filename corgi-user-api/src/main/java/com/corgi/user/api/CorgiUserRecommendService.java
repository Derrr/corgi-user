package com.corgi.user.api;

import com.corgi.user.entity.UserProfile;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserRecommendService {
    void clearRecUser(String userId);

    void followRecUser(String userId, String recId);

    void addRecUser(String userId, String recId);

    void updateRecStatus(String userId, String recId, String status);

    List<UserProfile> getRecUser(String userId, Integer size);

    List<UserProfile> getInfluencerByCity(String userId, String city, Integer size);

    List<UserProfile> getCityPopulate(String userId, String city, Integer page, Integer size);

    void deleteRecUserByWeight(String userId, Integer weight);

    void distLikeUser(String userId, String disLikeUserId);

    void initInfluencer();
}
