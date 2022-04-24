package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiViewMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiViewService;
import com.corgi.user.entity.ActivityView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiViewService.class)
@Slf4j
@Component
public class CorgiViewServiceImpl implements CorgiViewService {
    @Autowired
    private CorgiViewMapper corgiViewMapper;

    @Override
    public void addView(ActivityView activityView) {
        corgiViewMapper.addView(activityView);
        corgiViewMapper.addViewCount(activityView.getUserId(), activityView.getActivityId());
    }
}
