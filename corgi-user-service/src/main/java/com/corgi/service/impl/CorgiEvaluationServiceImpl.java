package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiEvaluationMapper;
import com.corgi.user.api.CorgiEvaluationService;
import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserEvaluation;
import com.corgi.user.entity.UserScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public List<UserEvaluation> getEvaluationByUser(String userId, String loginUserId, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getEvaluationByUser(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<UserEvaluation> getEvaluationByHeat(String userId, String loginUserId, Integer page, Integer size) {
        return corgiEvaluationMapper.getEvaluationByHeat(userId, (page - 1) * size, size);
    }

    @Override
    public List<UserEvaluation> getEvaluationByEvaluator(String evaluatorId, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getEvaluationByEvaluator(evaluatorId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<UserEvaluation> getEvaluationByTag(String userId, String tag, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getEvaluationByTag(userId, tag, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<UserEvaluation> getDateEvaluation(String applyId, String evaluatorId) {
        return corgiEvaluationMapper.getEvaluationByDate(applyId, evaluatorId);
    }

    @Override
    public UserEvaluation getEvaluationById(String id) {
        return corgiEvaluationMapper.getEvaluationById(id);
    }

    @Override
    public List<CorgiDateApply> getNeeEvaluation(String userId, Integer page, Integer pageSize) {
        return corgiEvaluationMapper.getNeedEvaluation(userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public String addEvaluation(UserEvaluation userEvaluation) {
        corgiEvaluationMapper.addEvaluation(userEvaluation);
        return userEvaluation.getId() + "";
    }

    @Override
    public void deleteEvaluation(UserEvaluation userEvaluation) {
        corgiEvaluationMapper.deleteEvaluation(userEvaluation.getId(), userEvaluation.getEvaluatorId());
        if (!StringUtils.isEmpty(userEvaluation.getTag())
                && StringUtils.isEmpty(userEvaluation.getEvaluatorId())
                && !StringUtils.isEmpty(userEvaluation.getUserId())) {
            corgiEvaluationMapper.deleteEvaluationByTag(userEvaluation.getTag(), userEvaluation.getUserId());
        }
    }

    @Override
    public UserScore getUserScore(String userId) {
        return corgiEvaluationMapper.getUserScore(userId);
    }


    @Override
    public void addUserScore(UserScore userScore) {
        corgiEvaluationMapper.initUserScore(userScore.getUserId());
        corgiEvaluationMapper.addUserScore(userScore);
    }

    @Override
    public Double getUserEvaluation(String userId) {
        Double result = corgiEvaluationMapper.getUserEvaluation(userId);
        if (result == null) {
            result = 0.0;
        }
        return result;
    }

    @Override
    public Double getTagScore(String tag) {
        return corgiEvaluationMapper.getTagEvaluation(tag);
    }

    @Override
    public Integer getUserCount(String userId) {
        return corgiEvaluationMapper.getUserCount(userId);
    }

    @Override
    public Integer countByTag(String tag, String userId) {
        return null;
    }

    @Override
    public void likeEvaluation(String userId, String evaluationId) {
        corgiEvaluationMapper.insertLike(userId, evaluationId);
        corgiEvaluationMapper.updateLike(userId, evaluationId, "1");
    }

    @Override
    public void unlikeEvaluation(String userId, String evaluationId) {
        corgiEvaluationMapper.updateLike(userId, evaluationId, "0");
    }

    @Override
    public List<String> getTags(String userId) {
        List<String> result = corgiEvaluationMapper.getUserTag(userId);
        if (result.size() < 6) {
            List<String> tmpResult = corgiEvaluationMapper.getUserTag(null);
            for (String tag : tmpResult) {
                if(!result.contains(tag)){
                    result.add(tag);
                }
            }
        }
        return result;
    }
}
