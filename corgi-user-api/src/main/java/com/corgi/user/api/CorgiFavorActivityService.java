package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFavorActivityService {
    boolean addFavor(String userId, String activityId);

    boolean deleteFavor(String userId, String activityId);

    List<String> getActivity(String userId, int start, int size);

    int countActivity(String userId, String activityId);
}
