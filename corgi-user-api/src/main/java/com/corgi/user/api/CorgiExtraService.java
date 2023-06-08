package com.corgi.user.api;

import com.corgi.user.entity.*;


/**
 * @author tairanliu
 */
public interface CorgiExtraService {
    UserExtra getUserExtra(String userId);

    void updateXp(String userId, String xp);

    void updateIncome(String userId, String income);

    void updateProfession(String userId, String profession);

    void updateEducation(String userId, String education);

    void updateInterests(String userId, String interests);

    void updateTags(String userId, String tags);
}
