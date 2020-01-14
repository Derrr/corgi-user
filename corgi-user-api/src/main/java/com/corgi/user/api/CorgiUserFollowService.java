package com.corgi.user.api;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserFollowService {
    boolean follow(String userId, String followUserId);

    boolean unfollow(String userId, String unFollowUserId);

    int isFollowed(String userId, String targetUserId);

    List<String> getFollowUser(String userId);
}
