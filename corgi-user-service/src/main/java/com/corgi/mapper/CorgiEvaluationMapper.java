package com.corgi.mapper;

import com.corgi.user.entity.CorgiDateApply;
import com.corgi.user.entity.UserEvaluation;
import com.corgi.user.entity.UserScore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiEvaluationMapper {

    List<UserEvaluation> getEvaluationByUser(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    List<UserEvaluation> getEvaluationByHeat(@Param("userId") String userId, @Param("size") Integer size);

    List<UserEvaluation> getEvaluationByEvaluator(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    List<UserEvaluation> getEvaluationByTag(@Param("userId") String userId, @Param("tag") String tag, @Param("start") Integer start, @Param("size") Integer size);

    void deleteEvaluation(@Param("id") Integer id, @Param("userId") String userId);

    void addEvaluation(@Param("evaluation") UserEvaluation userEvaluation);

    List<UserEvaluation> getEvaluationByDate(@Param("applyId") String applyId, @Param("evaluatorId") String evaluatorId);

    List<CorgiDateApply> getNeedEvaluation(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    UserScore getUserScore(@Param("userId") String userId);

    void initUserScore(@Param("userId") String userId);

    void addUserScore(@Param("score") UserScore userScore);

    Double getUserEvaluation(@Param("userId") String userId);

    Double getTagEvaluation(@Param("tag") String tag);

    Integer getUserCount(@Param("userId")String userId);

    void insertLike(@Param("userId") String userId, @Param("id") String id);

    void updateLike(@Param("userId") String userId, @Param("id") String id, @Param("status") String status);

    List<String> getUserTag(@Param("userId")String userId);
}
