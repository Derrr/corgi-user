package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiExtraService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiExtraService.class)
@Slf4j
@Component
public class CorgiExtraServiceImpl implements CorgiExtraService {

    @Autowired
    private CorgiUserMapper corgiUserMapper;


    @Override
    public List<String> getExtraUserIds(Integer page, Integer size) {
        return corgiUserMapper.getExtraUserIdByPage((page - 1) * size, size);
    }

    @Override
    public UserExtra getUserExtra(String userId) {
        UserExtra extra = corgiUserMapper.getUserExtra(userId);
        if (extra == null) {
            extra = new UserExtra();
        }
        return extra;
    }

    @Override
    public void updateXp(String userId, String xp) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateXp(userId, xp);
    }

    @Override
    public void updateAim(String userId, String aim) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateAim(userId, aim);
    }

    @Override
    public void updateIncome(String userId, String income) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateIncome(userId, income);
    }

    @Override
    public void updateProfession(String userId, String profession) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateProfession(userId, profession);
    }

    @Override
    public void updateEducation(String userId, String education) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateEducation(userId, education);
    }

    @Override
    public void updateInterests(String userId, String interests) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateInterests(userId, interests);
    }

    @Override
    public void updateTags(String userId, String tags) {
        corgiUserMapper.initUserExtra(userId);
        corgiUserMapper.updateTags(userId, tags);
    }
}
