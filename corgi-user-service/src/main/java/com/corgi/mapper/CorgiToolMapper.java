package com.corgi.mapper;

import com.corgi.entity.CorgiStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiToolMapper {
    /**
     * 获取话题
     *
     * @return
     */
    List<String> getTopics();

    /**
     * 添加话题
     *
     * @param topic
     */
    void addTopic(@Param("topic") String topic);

    /**
     * 删除话题
     *
     * @param topicId
     */
    void deleteTopic(@Param("topicId") String topicId);

    /**
     * 添加统计
     *
     * @param table
     * @param date
     * @param count
     */
    void addCount(@Param("table") String table, @Param("date") String date, @Param("count") Long count);

    /**
     * 删除统计
     *
     * @param table
     * @param date
     */
    void deleteCount(@Param("table") String table, @Param("date") String date);


    /**
     * 获取统计
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    List<CorgiStatistic> getCount(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取活动话题
     * @param activityId
     * @return
     */
    List<String> getActivityTopic(@Param("activityId") String activityId);

    /**
     * 添加活动话题
     * @param activityId
     * @param topic
     */
    void addActivityTopic(@Param("activityId") String activityId, @Param("topic") String topic);

    /**
     * 删除活动话题
     * @param activityId
     */
    void deleteActivityTopic(@Param("activityId") String activityId);

}
