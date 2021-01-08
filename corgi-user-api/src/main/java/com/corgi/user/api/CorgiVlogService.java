package com.corgi.user.api;


import com.corgi.user.entity.CorgiVlog;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVlogService {
    CorgiVlog getVlog(String activityId);

    void addVlog(CorgiVlog corgiVlog);

    void addVlogCount(CorgiVlog corgiVlog);
}
