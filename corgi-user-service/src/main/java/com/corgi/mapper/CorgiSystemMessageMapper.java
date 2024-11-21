package com.corgi.mapper;

import com.corgi.user.entity.MessageRecord;
import com.corgi.user.entity.MessageRule;
import com.corgi.user.entity.SystemMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiSystemMessageMapper {
    /**
     * 获取所有message
     *
     * @param filterTitle
     * @param start
     * @param size
     * @return
     */
    List<SystemMessage> getSystemMessageByPage(@Param("filterTitle") String filterTitle, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 统计message
     *
     * @param filterTitle
     * @return
     */
    Integer countSystemMessage(@Param("filterTitle") String filterTitle);

    /**
     * 获取message
     *
     * @param messageId
     * @return
     */
    SystemMessage getSystemMessageById(@Param("messageId") String messageId);

    /**
     * 获取指定状态及时间之前的message
     *
     * @param time
     * @param status
     * @return
     */
    List<SystemMessage> getSystemMessageByTime(@Param("time") Long time, @Param("status") String status);

    /**
     * 添加system message
     *
     * @param systemMessage
     */
    void addSystemMessage(@Param("message") SystemMessage systemMessage);

    /**
     * 更新system message
     *
     * @param systemMessage
     */
    void updateSystemMessage(@Param("message") SystemMessage systemMessage);

    /**
     * 获取
     *
     * @param messageId
     * @return
     */
    List<MessageRule> getMessageRuleById(@Param("messageId") String messageId);

    /**
     * 添加规则
     *
     * @param messageRule
     */
    void addMessageRule(@Param("rule") MessageRule messageRule);

    /**
     * 清除对应规则
     *
     * @param messageId
     */
    void deleteMessageRule(@Param("messageId") String messageId);

    /**
     * 分页获取推送记录
     *
     * @param start
     * @param size
     * @return
     */
    List<MessageRecord> getMessageRecordByPage(@Param("start") Integer start, @Param("size") Integer size);

    /**
     * 分页获取消息下推送记录
     *
     * @param messageId
     * @param start
     * @param size
     * @return
     */
    List<MessageRecord> getMessageRecordByMessageId(@Param("messageId") String messageId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 统计消息下推送记录
     *
     * @param messageId
     * @return
     */
    Integer countMessageRecordByMessageId(@Param("messageId") String messageId);

    /**
     * 添加记录
     *
     * @param messageRecord
     */
    void addMessageRecord(@Param("record") MessageRecord messageRecord);

    /**
     * 更新记录
     *
     * @param messageRecord
     */
    void updateMessageRecord(@Param("record") MessageRecord messageRecord);

    /**
     * 更新记录
     *
     * @param messageRecord
     */
    List<MessageRecord> searchMessageRecord(@Param("record") MessageRecord messageRecord, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 统计记录
     *
     * @param messageRecord
     */
    Integer countMessageRecord(@Param("record") MessageRecord messageRecord);


}
