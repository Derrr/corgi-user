package com.corgi.user.api;


import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.PushLog;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiPushLogService {
    void addPushLog(PushLog pushLog);

    Long countUsefulPush(String date);

    Long countTotalPush(String date);
}
