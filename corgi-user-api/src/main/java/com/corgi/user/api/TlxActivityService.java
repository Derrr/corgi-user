package com.corgi.user.api;

import com.corgi.user.entity.TlxActivity;

import java.util.List;

/**
 * @author tairanliu
 */
public interface TlxActivityService {
    List<TlxActivity> getActivityList(Integer page, Integer pageSize, TlxActivity city);

    TlxActivity getActivity(String id);

    void updateActivity(TlxActivity tlxActivity);

    void updateStatus(String id, String status);
}
