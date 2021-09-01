package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiShareMapper;
import com.corgi.user.api.CorgiShareService;
import com.corgi.user.entity.ActivityShare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

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

    @Override
    public String getShareToken(String userId, String type, String sourceId) {
        String token = "";
        for (int i = 0; i < 5; i++) {
            token = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
            if (corgiShareMapper.countToken(token) == 0) {
                break;
            }
        }
        if (StringUtils.isEmpty(sourceId)) {
            sourceId = "-";
        }
        corgiShareMapper.addToken(token, userId, type, sourceId);
        return token;
    }

    @Override
    public void viewShare(String token) {
        corgiShareMapper.viewToken(token);
    }
}
