package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.TlxActivityMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.TlxActivityService;
import com.corgi.user.entity.TlxActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = TlxActivityService.class)
@Slf4j
@Component
public class TlxActivityServiceImpl implements TlxActivityService {
    @Autowired
    private TlxActivityMapper tlxActivityMapper;

    @Override
    public List<TlxActivity> getActivityList(Integer page, Integer pageSize, TlxActivity activity) {
        return tlxActivityMapper.getActivityList(activity, (page - 1) * pageSize, pageSize);
    }

    @Override
    public TlxActivity getActivity(String id) {
        return tlxActivityMapper.getActivity(id);
    }

    @Override
    public void updateActivity(TlxActivity tlxActivity) {
        int result = tlxActivityMapper.addActivity(tlxActivity);
        if (result <= 0) {
            tlxActivityMapper.updateActivity(tlxActivity);
        }
    }

    @Override
    public void updateStatus(String id, String status) {
        tlxActivityMapper.updateStatus(id, status);
    }

    @Override
    public void refreshStatus(String version) {
        tlxActivityMapper.deleteActivityByVersion(version);
    }
}
