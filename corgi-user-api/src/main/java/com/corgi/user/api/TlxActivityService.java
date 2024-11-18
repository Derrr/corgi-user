package com.corgi.user.api;

import com.corgi.user.entity.TlxActivity;
import com.corgi.user.entity.UserDetail;

import java.util.List;

/**
 * @author tairanliu
 */
public interface TlxActivityService {
    List<TlxActivity> getActivityList(Integer page, Integer pageSize, TlxActivity city);

    TlxActivity getActivity(String id);

    void updateActivity(TlxActivity tlxActivity);

    void updateStatus(String id, String status);

    void refreshStatus(String version);

    Integer countActivityUser(String id);

    Integer countActivity(TlxActivity tlxActivity);

    List<UserDetail> getActivityUsers(String id);

    void addActivityUser(String id, String userId);

    void deleteActivityUser(String id, String userId);
}
