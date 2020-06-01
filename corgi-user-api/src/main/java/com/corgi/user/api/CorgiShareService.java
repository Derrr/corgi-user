package com.corgi.user.api;


import com.corgi.user.entity.ActivityLike;
import com.corgi.user.entity.ActivityShare;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiShareService {
    void addShare(ActivityShare activityShare);

    Integer countShare(String activityId);
}
