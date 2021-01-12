package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiVlogMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiVlogService.class)
@Slf4j
@Component
public class CorgiVlogServiceImpl implements CorgiVlogService {
    @Autowired
    private CorgiVlogMapper corgiVlogMapper;

    @Override
    public CorgiVlog getVlog(String activityId) {
        return corgiVlogMapper.getVlogById(activityId);
    }

    @Override
    public void addVlog(CorgiVlog corgiVlog) {
        corgiVlogMapper.addVlog(corgiVlog);
    }

    @Override
    public void addVlogCount(CorgiVlog corgiVlog) {
        corgiVlogMapper.addVlogCount(corgiVlog);
    }

    @Override
    public List<CorgiVlog> recallVlog(CorgiVlog corgiVlog, Integer limit) {
        return corgiVlogMapper.recallVlog(corgiVlog, limit, UserUtils.getIndex(corgiVlog.getUserId()));
    }
}
