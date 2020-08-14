package com.corgi.user.api;

import com.corgi.user.entity.HotActivity;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiHotActivityService {
    List<HotActivity> getHotActivityList();

    List<HotActivity> getListByCity(String city);

    void updateBarProfile(HotActivity hotActivity);

    void addBarProfile(HotActivity hotActivity);

    void deleteBarProfile(String hotId);

    List<HotActivity> searchBar(HotActivity hotActivity);
}
