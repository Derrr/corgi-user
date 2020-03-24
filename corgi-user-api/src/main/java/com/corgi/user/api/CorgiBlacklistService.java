package com.corgi.user.api;

import com.corgi.user.entity.UserBasic;
import com.corgi.user.entity.UserProfile;

import java.util.List;

public interface CorgiBlacklistService {
    void addBlacklist(String userId, String blackId);

    void deleteBlacklist(String userId, String blackId);

    List<UserBasic> getBlackUser(String userId);
}
