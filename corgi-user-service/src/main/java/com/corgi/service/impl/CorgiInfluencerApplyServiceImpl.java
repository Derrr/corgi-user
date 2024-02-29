package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiInfluencerApplyMapper;
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
    @Autowired
    private CorgiInfluencerApplyMapper corgiInfluencerApplyMapper;

    @Override
    public void addApply(InfluencerApply apply) {
        corgiInfluencerApplyMapper.addApply(apply);
    }

    @Override
    public void updateApply(InfluencerApply apply) {
        corgiInfluencerApplyMapper.updateApply(apply);
    }

    @Override
    public List<InfluencerApply> getApplies(InfluencerApply apply, Integer page, Integer size) {
        return corgiInfluencerApplyMapper.getApplies(apply,(page - 1) * size, size);
    }

    @Override
    public Integer countApplies(InfluencerApply apply) {
        return corgiInfluencerApplyMapper.countApplies(apply);
    }
}
