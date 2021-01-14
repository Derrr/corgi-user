package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.CorgiActivity;
import com.corgi.mapper.CorgiBarMapper;
import com.corgi.mapper.CorgiFakeMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.BarProfile;
import com.corgi.user.entity.UserQuery;
import com.corgi.user.entity.UserVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiFakeService.class)
@Slf4j
@Component
public class CorgiFakeServiceImpl implements CorgiFakeService {
    @Autowired
    private CorgiFakeMapper corgiFakeMapper;

    @Override
    public void refreshFakeUser(Integer size) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        corgiFakeMapper.clearFakeUserPool();
        corgiFakeMapper.initFakeUserPool(1000, calendar.getTimeInMillis());
    }

    @Override
    public String selectFakeUser() {
        Integer count = corgiFakeMapper.countFakeUser();
        Random random = new Random();
        return corgiFakeMapper.selectFakeUser(random.nextInt(count));
    }
}
