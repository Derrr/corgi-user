package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserTagService {
    List<String> getTags();

    List<String> getUserTags(String userId);

    List<String> getInterestsByCategory(String category);

    List<UserInterest> getUserInterest(String userId);

    void updateUserTag(String userId, List<String> tags);

    void updateUserInterest(String userId, String category, List<String> interests);

}
