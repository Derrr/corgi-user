package com.corgi.service.impl;

import com.corgi.mapper.CorgiFavorActivityMapper;
import com.corgi.user.api.CorgiFavorActivityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author tairanliu
 */
public class CorgiFavorActivityImpl implements CorgiFavorActivityService {
    @Autowired
    private CorgiFavorActivityMapper favorActivityMapper;

    @Override
    public boolean addFavor(String userId, String activityId) {
        favorActivityMapper.addFavor(userId, activityId);
        return true;
    }

    @Override
    public boolean deleteFavor(String userId, String activityId) {
        favorActivityMapper.deleteFavor(userId, activityId);
        return false;
    }

    @Override
    public List<String> getActivity(String userId, int start, int size) {
        return favorActivityMapper.getActivity(userId, start, size);
    }

    @Override
    public int countActivity(String userId, String activityId) {
        return favorActivityMapper.countActivity(userId, activityId);
    }
}
