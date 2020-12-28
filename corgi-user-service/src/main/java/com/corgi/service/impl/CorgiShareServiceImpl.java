package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiShareMapper;
import com.corgi.user.api.CorgiShareService;
import com.corgi.user.entity.ActivityShare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(interfaceClass = CorgiShareService.class)
@Slf4j
@Component
public class CorgiShareServiceImpl implements CorgiShareService {
    @Autowired
    private CorgiShareMapper corgiShareMapper;

    @Override
    public void addShare(ActivityShare activityShare) {
        corgiShareMapper.addActivityShare(activityShare);
    }

    @Override
    public Integer countShare(String activityId) {
        return corgiShareMapper.countActivityShare(activityId);
    }
}
