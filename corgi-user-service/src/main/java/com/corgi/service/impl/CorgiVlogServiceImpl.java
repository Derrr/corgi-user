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

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Reference
    private CorgiFeedService corgiFeedService;

    @Override
    public CorgiVlog getVlog(String activityId) {
        return corgiVlogMapper.getVlogById(activityId);
    }

    @Override
    public void deleteVlog(String activityId) {
        corgiVlogMapper.deleteVlog(activityId);
    }

    @Override
    public void failVlog(String activityId) {
        corgiVlogMapper.failVlog(activityId);
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

    @Override
    public List<CorgiVlog> getFollowVlog(String userId, Integer page, Integer size) {
        return corgiVlogMapper.getFollowVlog(userId, (page - 1) * size, size, getNowDate());
    }

    @Override
    public List<CorgiVlog> getUserVlog(String userId, Integer page, Integer size) {
        return corgiVlogMapper.getUserVlog(userId, (page - 1) * size, size, getNowDate());
    }

    private String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
