package com.corgi.user.api;

/**
 * @author tairanliu
 */
public interface CorgiUserFollowService {
    boolean follow(String userId, String followUserId);

    boolean unFollow(String userId, String unFollowUserId);

    int isFollowed(String userId, String targetUserId);
}
