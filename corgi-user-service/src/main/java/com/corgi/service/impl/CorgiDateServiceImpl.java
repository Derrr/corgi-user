package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiDateMapper;
import com.corgi.user.api.CorgiUserDateService;
import com.corgi.user.entity.CorgiDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserDateService.class)
@Slf4j
@Component
public class CorgiDateServiceImpl implements CorgiUserDateService {
    @Autowired
    private CorgiDateMapper corgiDateMapper;

    @Override
    public void addDate(CorgiDate date) {
        if (StringUtils.isEmpty(date.getStatus())) {
            CorgiDate oldDate = corgiDateMapper.getDateByUserId(date.getUserId());
            if (oldDate == null) {
                date.setStatus(CorgiDate.OPEN);
            } else {
                date.setStatus(oldDate.getStatus());
            }
        }
        corgiDateMapper.addCorgiDate(date);
    }

    @Override
    public List<CorgiDate> searchDate(CorgiDate date) {
        return corgiDateMapper.searchDate(date);
    }

    @Override
    public void updateDate(CorgiDate date) {
        corgiDateMapper.updateCorgiDate(date);
    }

    @Override
    public CorgiDate getDateByUserId(String userId) {
        return corgiDateMapper.getDateByUserId(userId);
    }
}
