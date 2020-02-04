package com.corgi.user.api;

import com.corgi.entity.CorgiStatistic;

import java.util.HashMap;
import java.util.List;

public interface CorgiStatisticService {
    void addCount(String table, String date, Long count);

    List<CorgiStatistic> getCount(String table, String beginDate, String endDate);

    long sumCount(String table, String beginDate, String endDate);

    List<HashMap> getMap(String table, String beginDate, String endDate);

    void initMap(String table, String date);

    void updateMap(String table, String date, String key, Long count);

    void addList(String table, String date, String name, Long count);
}
