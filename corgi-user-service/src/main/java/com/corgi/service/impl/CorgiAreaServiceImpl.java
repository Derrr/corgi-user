package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.user.api.CorgiAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiAreaService.class)
@Slf4j
@Component
public class CorgiAreaServiceImpl implements CorgiAreaService {
    @Autowired
    private CorgiAreaMapper corgiAreaMapper;

    @Override
    public List<CorgiArea> getAreaByCity(String city) {
        return corgiAreaMapper.getArea(CorgiArea.builder().city(city).build());
    }

    @Override
    public List<CorgiArea> getAreaByType(String city, String type) {
        return corgiAreaMapper.getArea(CorgiArea.builder().city(city).type(type).build());
    }

    @Override
    public void addArea(CorgiArea corgiArea) {
        corgiAreaMapper.addArea(corgiArea);
    }
}
