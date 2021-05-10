package com.corgi.user.api;


import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserEvaluation;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiEvaluationService {
    List<UserEvaluation> getEvaluationByUser(String userId);

    List<UserEvaluation> getEvaluationByEvaluator(String evaluatorId, Integer page, Integer pageSize);

    List<UserEvaluation> getDateEvaluation(String applyId, String evaluatorId);

    List<CorgiDateApply> getNeeEvaluation(String userId, Integer page, Integer pageSize);

    void addEvaluation(UserEvaluation userEvaluation);

    void deleteEvaluation(UserEvaluation userEvaluation);
}
