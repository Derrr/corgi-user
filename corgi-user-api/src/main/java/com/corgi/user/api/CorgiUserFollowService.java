package com.corgi.user.api;

import com.corgi.user.entity.UserProfile;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserFollowService {
    boolean follow(String userId, String followUserId);

    boolean unfollow(String userId, String unFollowUserId);

    int isFollowed(String userId, String targetUserId);

    int countFollow(String userId);

    int countMatch(String userId);

    int countFollowed(String userId);

    int countRealFollowed(String userId);

    int countAllFollowed(String userId);

    List<String> getFollowUser(String userId);

    List<UserProfile> getFollowUserByPage(String userId, String type, Double lat, Double lng, Integer page, Integer pageSize);

    List<UserProfile> getMatchUserByPage(String userId, String type, Double lat, Double lng, Integer page, Integer pageSize);

    List<UserProfile> getFollowedUserByPage(String userId, long time, Integer page, Integer pageSize);

    List<UserProfile> getFollowUserHistoryByPage(String userId, Integer page, Integer pageSize);

    void readFollowUser(String userId, String followedUserId);

    List<UserProfile> getAllFollowUserByPage(Integer page, Integer pageSize);

    List<UserProfile> getShareUserByPage(String userId, String name, Integer page, Integer pageSize);
}
