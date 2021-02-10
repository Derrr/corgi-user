package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiVisitMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiVisitService;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiVisitService.class)
@Slf4j
@Component
public class CorgiVisitServiceImpl implements CorgiVisitService {
    @Autowired
    private CorgiVisitMapper corgiVisitMapper;

    @Override
    public void visit(String userId, String toId) {
        corgiVisitMapper.addVisit(toId, userId);
        corgiVisitMapper.addVisitCount(toId, userId);
    }

    @Override
    public List<UserProfile> getVisitor(String userId, Integer limit) {
        return corgiVisitMapper.getVisitor(userId, limit);
    }
}
