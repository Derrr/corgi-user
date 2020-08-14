package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiBarMapper;
import com.corgi.mapper.CorgiHotActivityMapper;
import com.corgi.user.api.CorgiBarService;
import com.corgi.user.api.CorgiHotActivityService;
import com.corgi.user.api.CorgiUserFollowService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.BarProfile;
import com.corgi.user.entity.HotActivity;
import com.corgi.user.entity.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiHotActivityService.class)
@Slf4j
@Component
public class CorgiHotActivityServiceImpl implements CorgiHotActivityService {

    @Autowired
    private CorgiHotActivityMapper corgiHotActivityMapper;

    @Override
    public List<HotActivity> getHotActivityList() {
        return corgiHotActivityMapper.getHotActivityList();
    }

    @Override
    public List<HotActivity> getListByCity(String city) {
        return corgiHotActivityMapper.getListByCity(city);
    }

    @Override
    public void updateBarProfile(HotActivity hotActivity) {
        corgiHotActivityMapper.updateHotActivity(hotActivity);
    }

    @Override
    public void addBarProfile(HotActivity hotActivity) {
        corgiHotActivityMapper.addHotActivity(hotActivity);
    }

    @Override
    public void deleteBarProfile(String hotId) {
        corgiHotActivityMapper.deleteHotActivity(hotId);
    }

    @Override
    public List<HotActivity> searchBar(HotActivity hotActivity) {
        return corgiHotActivityMapper.searchHotActivity(hotActivity);
    }
}
