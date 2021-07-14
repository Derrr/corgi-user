package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiInfluencerApplyService;
import com.corgi.user.entity.InfluencerApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiInfluencerApplyService.class)
@Slf4j
@Component
public class CorgiInfluencerApplyServiceImpl implements CorgiInfluencerApplyService {

    @Override
    public void addApply(InfluencerApply apply) {

    }

    @Override
    public List<InfluencerApply> getApplies(Integer page, Integer size) {
        return null;
    }
}
