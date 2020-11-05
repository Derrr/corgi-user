package com.corgi.user.api;

import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBarService {
    List<BarProfile> getBarList(String status);

    List<BarProfile> getBarAccountList(String status);

    List<BarProfile> getBarListByCity(String city);

    BarProfile getBarProfile(String barId);

    void updateBarProfile(BarProfile barProfile);

    void addBarProfile(BarProfile barProfile);

    List<BarProfile> searchBar(BarProfile barProfile);

    void setBarAccount(String barId, String account, String password);

    BarProfile getBarByAccount(String account, String password);
}
