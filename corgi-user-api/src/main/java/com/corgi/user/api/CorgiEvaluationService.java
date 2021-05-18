package com.corgi.user.api;


import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserEvaluation;
import com.corgi.user.entity.UserScore;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiEvaluationService {
    List<UserEvaluation> getEvaluationByUser(String userId,String loginUserId, Integer page, Integer pageSize);

    List<UserEvaluation> getEvaluationByHeat(String userId,String loginUserId, Integer pageSize);

    List<UserEvaluation> getEvaluationByEvaluator(String evaluatorId, Integer page, Integer pageSize);

    List<UserEvaluation> getEvaluationByTag(String userId, String tag, Integer page, Integer pageSize);

    List<UserEvaluation> getDateEvaluation(String applyId, String evaluatorId);

    List<CorgiDateApply> getNeeEvaluation(String userId, Integer page, Integer pageSize);

    void addEvaluation(UserEvaluation userEvaluation);

    void deleteEvaluation(UserEvaluation userEvaluation);

    UserScore getUserScore(String userId);

    void addUserScore(UserScore userScore);

    Double getUserEvaluation(String userId);

    Double getTagScore(String tag);

    Integer getUserCount(String userId);

    void likeEvaluation(String userId, String evaluationId);

    void unlikeEvaluation(String userId, String evaluationId);
}
