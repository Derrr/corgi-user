package com.corgi.mapper;

import com.corgi.entity.CorgiTopic;
import com.corgi.user.entity.ActivityMessage;
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
     * 获取话题
     *
     * @param id
     * @return
     */
    CorgiTopic getTopicById(@Param("id") String id);

    /**
     * 获取活动类型
     *
     * @return
     */
    List<String> getActivityTypes();

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
     * 根据话题获取活动
     *
     * @param topic
     * @param start
     * @param size
     * @return
     */
    List<String> getActivityIdsByTopic(@Param("topic") String topic, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 修改活动权重
     *
     * @param activityId
     * @param weight
     */
    void updateActivityWeight(@Param("activityId") String activityId, @Param("weight") Integer weight);

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
    void addCountByUser(@Param("user") String user, @Param("count") Integer count);

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
     *
     * @return
     */
    List<HashMap> getAllInfluencer();

    /**
     * 添加活动消息
     *
     * @param activityMessage
     */
    void addActivityMessage(@Param("message") ActivityMessage activityMessage);

    /**
     * 获取活动消息
     *
     * @param userId
     * @param size
     * @return
     */
    List<ActivityMessage> getActivityMessage(@Param("userId") String userId, @Param("size") Integer size);

    /**
     * 根据消息类型获取活动消息
     *
     * @param userId
     * @param size
     * @param type
     * @return
     */
    List<ActivityMessage> getActivityMessageByType(@Param("userId") String userId, @Param("size") Integer size, @Param("type") String type);

    /**
     * 对特定类型message计数
     *
     * @param message
     * @return
     */
    Integer countActivityMessageByMessage(@Param("message") ActivityMessage message);

    /**
     * 获取所有活动消息
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<ActivityMessage> getAllActivityMessage(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 根据类型获取所有活动消息
     *
     * @param userId
     * @param type
     * @param start
     * @param size
     * @return
     */
    List<ActivityMessage> getAllActivityMessageByType(@Param("userId") String userId, @Param("type") String type, @Param("start") Integer start, @Param("size") Integer size);


    /**
     * 获取消息数
     *
     * @param userId
     * @return
     */
    Long countActivityMessage(@Param("userId") String userId);

    /**
     * 删除活动消息
     *
     * @param userId
     * @param time
     */
    void deleteActivityMessage(@Param("userId") String userId, @Param("time") Long time);

    /**
     * 根据条件删除活动信息
     *
     * @param message
     */
    void deleteActivityMessageByMessage(@Param("message") ActivityMessage message);

    /**
     * 已读消息
     *
     * @param userId
     */
    void readActivityMessage(@Param("userId") String userId);

    /**
     * 已读消息
     *
     * @param userId
     * @param type
     */
    void readActivityMessageByType(@Param("userId") String userId, @Param("type") String type);

    /**
     * 绑定微信ID
     *
     * @param wechatId
     * @param corgiId
     */
    void bindWechatId(@Param("wechatId") String wechatId, @Param("corgiId") String corgiId);

    /**
     * 根据wechat id获取ID
     *
     * @param wechatId
     * @return
     */
    String getIdByWechat(@Param("wechatId") String wechatId);

}
