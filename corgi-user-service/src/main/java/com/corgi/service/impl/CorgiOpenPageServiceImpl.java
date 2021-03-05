package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiOpenPageMapper;
import com.corgi.user.api.CorgiOpenPageService;
import com.corgi.user.entity.CorgiOpenPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiOpenPageService.class)
@Slf4j
@Component
public class CorgiOpenPageServiceImpl implements CorgiOpenPageService {
    @Autowired
    private CorgiOpenPageMapper corgiOpenPageMapper;

    @Override
    public void addOpenPage(CorgiOpenPage corgiOpenPage) {
        corgiOpenPageMapper.addOpenPage(corgiOpenPage);
    }

    @Override
    public void deleteOpenPage(Integer bannerId) {
        corgiOpenPageMapper.deleteOpenPage(bannerId);
    }

    @Override
    public void updateOpenPage(CorgiOpenPage corgiOpenPage) {
        corgiOpenPageMapper.updateOpenPage(corgiOpenPage);
    }

    @Override
    public List<CorgiOpenPage> listOpenPage(CorgiOpenPage corgiOpenPage) {
        return corgiOpenPageMapper.listOpenPage(corgiOpenPage);
    }

    @Override
    public List<CorgiOpenPage> getBirthdayOpenPage(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("/MM/dd");
        Long time = System.currentTimeMillis() - 30 * 24 * 3600 * 1000;
        return corgiOpenPageMapper.getBirthdayOpenPage(userId, sdf.format(new Date()), time);
    }
}
