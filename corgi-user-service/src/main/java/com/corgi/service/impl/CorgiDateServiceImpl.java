package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.user.api.CorgiUserDateService;
import com.corgi.user.entity.CorgiDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserDateService.class)
@Slf4j
@Component
public class CorgiDateServiceImpl implements CorgiUserDateService {

    @Override
    public void addDate(CorgiDate date) {

    }

    @Override
    public List<CorgiDate> searchDate(CorgiDate date) {
        return null;
    }

    @Override
    public void updateDate(CorgiDate date) {

    }

    @Override
    public CorgiDate getDateById(Integer id) {
        return null;
    }
}
