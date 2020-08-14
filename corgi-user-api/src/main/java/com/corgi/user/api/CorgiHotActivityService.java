package com.corgi.user.api;

import com.corgi.user.entity.HotActivity;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiHotActivityService {
    List<HotActivity> getHotActivityList();

    List<HotActivity> getListByCity(String city);

    void updateHotActivity(HotActivity hotActivity);

    void addHotActivity(HotActivity hotActivity);

    void deleteHotActivity(String hotId);

    List<HotActivity> searchHotActivity(HotActivity hotActivity);
}
