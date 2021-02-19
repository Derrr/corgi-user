package com.corgi.user.api;


import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVlogService {
    CorgiVlog getVlog(String activityId);

    void deleteVlog(String activityId);

    void addVlog(CorgiVlog corgiVlog);

    void addVlogCount(CorgiVlog corgiVlog);

    List<CorgiVlog> recallVlog(CorgiVlog corgiVlog, Integer limit);

    List<CorgiVlog> recallTargetVlog(String targetId, CorgiVlog corgiVlog, Integer limit);

    List<CorgiVlog> recallHotVlog(CorgiVlog corgiVlog, Integer limit);

    List<CorgiVlog> getFollowVlog(String userId, Integer page, Integer size);

    List<CorgiVlog> getUserVlog(String userId, Integer page, Integer size);

    void addHotVlog(CorgiVlogHot corgiVlogH);

    void updateHotVlog(CorgiVlogHot corgiVlogHot);

    List<CorgiVlogHot> getHotVlog(CorgiVlogHot corgiVlogHot, Integer page, Integer pageSize);

    Integer countHotVlog(CorgiVlogHot corgiVlogHot);

    Integer countVlog(String date);
}
