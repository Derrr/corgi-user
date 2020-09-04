package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiPushLogMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiPushLogService;
import com.corgi.user.entity.PushLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiPushLogService.class)
@Slf4j
@Component
public class CorgiPushLogServiceImpl implements CorgiPushLogService {
    @Autowired
    private CorgiPushLogMapper corgiPushLogMapper;

    @Override
    public void addPushLog(PushLog pushLog) {
        corgiPushLogMapper.addPushLog(pushLog);
    }

    @Override
    public Long countUsefulPush(String date) {
        return corgiPushLogMapper.countUsefulPush(date);
    }

    @Override
    public Long countTotalPush(String date) {
        return corgiPushLogMapper.countTotalPush(date);
    }
}
