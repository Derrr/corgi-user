package com.corgi.mapper;

import com.corgi.user.entity.UserEvaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiEvaluationMapper {

    List<UserEvaluation> getEvaluationByUser(@Param("userId") String userId);

    List<UserEvaluation> getEvaluationByEvaluator(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    void deleteEvaluation(@Param("id") Integer id, @Param("userId") String userId);

    void addEvaluation(@Param("evaluation") UserEvaluation userEvaluation);
}
