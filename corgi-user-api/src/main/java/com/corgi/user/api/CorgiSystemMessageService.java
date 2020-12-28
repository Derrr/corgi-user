package com.corgi.user.api;

import com.corgi.user.entity.MessageRecord;
import com.corgi.user.entity.MessageRule;
import com.corgi.user.entity.SystemMessage;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiSystemMessageService {
    List<SystemMessage> getSystemMessageByPage(Integer page, Integer pageSize);

    SystemMessage getMessageDetail(String messageId);

    List<SystemMessage> getSystemMessagesByTime(Long time);

    void addSystemMessage(SystemMessage systemMessage);

    void updateSystemMessage(SystemMessage systemMessage);

    List<MessageRule> getMessageRule(String messageId);

    void addMessageRule(MessageRule messageRule);

    void deleteMessageRule(String messageId);

    List<MessageRecord> getMessageRecordByPage(Integer page, Integer pageSize);

    List<MessageRecord> getMessageRecordByMessageId(Integer page, Integer pageSize, String messageId);

    String addMessageRecord(MessageRecord messageRecord);

    void updateMessageRecord(MessageRecord messageRecord);
}
