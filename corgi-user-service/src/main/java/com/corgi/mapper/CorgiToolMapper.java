package com.corgi.mapper;

import com.corgi.entity.CorgiStatistic;
import com.corgi.entity.CorgiTopic;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiToolMapper {

    /**
     * 搜索话题
     *
     * @param text
     * @param status
     * @return
     */
    List<CorgiTopic> searchTopic(@Param("text") String text, @Param("status") String status);


    /**
     * 添加话题
     *
     * @param topic
     */
    void addTopic(@Param("topic") CorgiTopic topic);

    /**
     * 修改话题
     *
     * @param topic
     */
    void updateTopic(@Param("topic") CorgiTopic topic);

    /**
     * 获取活动话题
     *
     * @param activityId
     * @return
     */
    List<String> getActivityTopic(@Param("activityId") String activityId);

    /**
     * 添加活动话题
     *
     * @param activityId
     * @param topic
     */
    void addActivityTopic(@Param("activityId") String activityId, @Param("topic") String topic);

    /**
     * 删除活动话题
     *
     * @param activityId
     */
    void deleteActivityTopic(@Param("activityId") String activityId);

    /**
     * 技数统计
     *
     * @param user
     * @param count
     */
    void addCountByUser(@Param("user") String user,@Param("count")Integer count);

    /**
     * 技数统计
     *
     * @param user
     * @return integer
     */
    Integer getCountByUser(@Param("user") String user);

    /**
     * 技数统计
     *
     * @param user
     * @param count
     */
    void updateCountByUser(@Param("user") String user, @Param("count") Integer count);

    /**
     * 获取所有人
     * @return
     */
    List<HashMap> getAllInfluencer();

}
