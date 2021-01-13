package com.corgi.user.api;


import com.corgi.user.entity.CorgiVlog;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVlogService {
    CorgiVlog getVlog(String activityId);

    void deleteVlog(String activityId);

    void failVlog(String activityId);

    void addVlog(CorgiVlog corgiVlog);

    void addVlogCount(CorgiVlog corgiVlog);

    List<CorgiVlog> recallVlog(CorgiVlog corgiVlog, Integer limit);

    List<CorgiVlog> getFollowVlog(String userId, Integer page, Integer size);

    List<CorgiVlog> getUserVlog(String userId, Integer page, Integer size);
}
