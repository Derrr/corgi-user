package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiEvaluationMapper;
import com.corgi.user.api.CorgiEvaluationService;
import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiEvaluationService.class)
@Slf4j
@Component
public class CorgiEvaluationServiceImpl implements CorgiEvaluationService {
    @Autowired
    private CorgiEvaluationMapper corgiEvaluationMapper;

    @Override
    public List<UserEvaluation> getEvaluationByUser(String userId) {
        return corgiEvaluationMapper.getEvaluationByUser(userId);
    }

    @Override
    public List<UserEvaluation> getEvaluationByEvaluator(String evaluatorId, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getEvaluationByEvaluator(evaluatorId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<UserEvaluation> getDateEvaluation(String applyId, String evaluatorId) {
        return corgiEvaluationMapper.getEvaluationByDate(applyId, evaluatorId);
    }

    @Override
    public List<CorgiDateApply> getNeeEvaluation(String userId, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getNeedEvaluation(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public void addEvaluation(UserEvaluation userEvaluation) {
        corgiEvaluationMapper.addEvaluation(userEvaluation);
    }

    @Override
    public void deleteEvaluation(UserEvaluation userEvaluation) {
        corgiEvaluationMapper.deleteEvaluation(userEvaluation.getId(), userEvaluation.getEvaluatorId());
    }
}
