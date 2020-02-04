package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiStatistic;
import com.corgi.mapper.CorgiStatisticMapper;
import com.corgi.user.api.CorgiStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiStatisticService.class)
@Slf4j
@Component
public class CorgiStatisticServiceImpl implements CorgiStatisticService {
    @Autowired
    private CorgiStatisticMapper corgiStatisticMapper;

    @Override
    public void addCount(String table, String date, Long count) {
        corgiStatisticMapper.deleteCount(table, date);
        corgiStatisticMapper.addCount(table, date, count);
    }

    @Override
    public List<CorgiStatistic> getCount(String table, String beginDate, String endDate) {
        return corgiStatisticMapper.getCount(table, beginDate, endDate);
    }

    @Override
    public long sumCount(String table, String beginDate, String endDate) {
        return corgiStatisticMapper.sumCount(table, beginDate, endDate);
    }

    @Override
    public List<HashMap> getMap(String table, String beginDate, String endDate) {
        return corgiStatisticMapper.getMap(table, beginDate, endDate);
    }

    @Override
    public void initMap(String table, String date) {
        corgiStatisticMapper.initMap(date, table);
    }

    @Override
    public void updateMap(String table, String date, String key, Long count) {
        List<HashMap> map = corgiStatisticMapper.getMap(table, date, date);
        if (CollectionUtils.isEmpty(map)) {
            corgiStatisticMapper.initMap(date, table);
        }
        corgiStatisticMapper.updateMap(table, date, key, count);
    }

    @Override
    public void addList(String table, String date, String name, Long count) {
        corgiStatisticMapper.addList(table, date, name, count);
    }

    @Override
    public List<HashMap> getList(String table, String beginDate, String endDate) {
        List<CorgiStatistic> results = corgiStatisticMapper.getList(table, beginDate, endDate);
        if (CollectionUtils.isEmpty(results)) {
            return new ArrayList<>();
        }
        HashMap<String, HashMap> hashResult = new HashMap<>();
        for (CorgiStatistic corgiStatistic : results) {
            String date = corgiStatistic.getDate();
            HashMap dateResult = hashResult.get(corgiStatistic.getDate());
            if(dateResult == null){
                dateResult = new HashMap();
                dateResult.put("date",date);
            }
            dateResult.put(corgiStatistic.getName(),corgiStatistic.getCount());
            hashResult.put(date,dateResult);
        }
        return new ArrayList<>(hashResult.values());
    }
}
