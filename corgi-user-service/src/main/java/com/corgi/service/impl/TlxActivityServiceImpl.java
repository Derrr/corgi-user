package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.TlxActivityMapper;
import com.corgi.mapper.TlxActivityUserMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.TlxActivityService;
import com.corgi.user.entity.TlxActivity;
import com.corgi.user.entity.UserDetail;
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
    @Autowired
    private TlxActivityUserMapper tlxActivityUserMapper;

    @Override
    public List<TlxActivity> getActivityList(Integer page, Integer pageSize, TlxActivity activity) {
        List<TlxActivity> results = tlxActivityMapper.getActivityList(activity, (page - 1) * pageSize, pageSize);
        for (TlxActivity tlx : results) {
            tlx.setHot(tlxActivityUserMapper.countActivityUser(tlx.getId()) + "");
        }
        return results;
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

    @Override
    public Integer countActivityUser(String id) {
        return tlxActivityUserMapper.countActivityUser(id);
    }

    @Override
    public Integer countActivity(TlxActivity tlxActivity) {
        return tlxActivityMapper.countActivity(tlxActivity);
    }

    @Override
    public List<UserDetail> getActivityUsers(String id) {
        return tlxActivityUserMapper.getActivityUsers(id);
    }

    @Override
    public void addActivityUser(String id, String userId) {
        tlxActivityUserMapper.deleteActivityUser(id, userId);
        tlxActivityUserMapper.addActivityUser(id, userId);
    }

    @Override
    public void deleteActivityUser(String id, String userId) {
        tlxActivityUserMapper.deleteActivityUser(id, userId);
    }
}
