package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiSystemMessageMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiSystemMessageService;
import com.corgi.user.entity.MessageRecord;
import com.corgi.user.entity.MessageRecordPage;
import com.corgi.user.entity.MessageRule;
import com.corgi.user.entity.SystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiSystemMessageService.class)
@Slf4j
@Component
public class CorgiSystemMessageServiceImpl implements CorgiSystemMessageService {

    @Autowired
    private CorgiSystemMessageMapper corgiSystemMessageMapper;


    @Override
    public List<SystemMessage> getSystemMessageByPage(Integer page, Integer pageSize) {
        List<SystemMessage> messages = corgiSystemMessageMapper.getSystemMessageByPage((page - 1) * pageSize, pageSize);
        if (messages != null) {
            for (SystemMessage message : messages) {
                message.setRules(corgiSystemMessageMapper.getMessageRuleById(message.getId()));
            }
        }
        return messages;
    }

    @Override
    public SystemMessage getMessageDetail(String messageId) {
        SystemMessage systemMessage = corgiSystemMessageMapper.getSystemMessageById(messageId);
        List<MessageRule> messageRules = corgiSystemMessageMapper.getMessageRuleById(messageId);
        systemMessage.setRules(messageRules);
        return systemMessage;
    }

    @Override
    public List<SystemMessage> getSystemMessagesByTime(Long time) {
        return corgiSystemMessageMapper.getSystemMessageByTime(time, SystemMessage.STATUS_CREATED);
    }

    @Override
    public void addSystemMessage(SystemMessage systemMessage) {
        corgiSystemMessageMapper.addSystemMessage(systemMessage);
        if (!CollectionUtils.isEmpty(systemMessage.getRules())) {
            for (MessageRule messageRule : systemMessage.getRules()) {
                messageRule.setMessageId(systemMessage.getId());
                corgiSystemMessageMapper.addMessageRule(messageRule);
            }
        }
    }

    @Override
    public void updateSystemMessage(SystemMessage systemMessage) {
        if (!StringUtils.isEmpty(systemMessage.getContent()) || !StringUtils.isEmpty(systemMessage.getStatus()) || systemMessage.getSentTime() != null || !StringUtils.isEmpty(systemMessage.getTitle())) {
            corgiSystemMessageMapper.updateSystemMessage(systemMessage);
        } else {
            corgiSystemMessageMapper.deleteMessageRule(systemMessage.getId());
        }
        if (!CollectionUtils.isEmpty(systemMessage.getRules())) {
            corgiSystemMessageMapper.deleteMessageRule(systemMessage.getId());
            for (MessageRule messageRule : systemMessage.getRules()) {
                messageRule.setMessageId(systemMessage.getId());
                corgiSystemMessageMapper.addMessageRule(messageRule);
            }
        }
    }

    @Override
    public List<MessageRule> getMessageRule(String messageId) {
        return corgiSystemMessageMapper.getMessageRuleById(messageId);
    }

    @Override
    public void addMessageRule(MessageRule messageRule) {
        corgiSystemMessageMapper.addMessageRule(messageRule);
    }

    @Override
    public void deleteMessageRule(String messageId) {
        corgiSystemMessageMapper.deleteMessageRule(messageId);
    }

    @Override
    public List<MessageRecord> getMessageRecordByPage(Integer page, Integer pageSize) {
        return corgiSystemMessageMapper.getMessageRecordByPage((page - 1) * pageSize, pageSize);
    }

    @Override
    public List<MessageRecord> getMessageRecordByMessageId(Integer page, Integer pageSize, String messageId) {
        return corgiSystemMessageMapper.getMessageRecordByMessageId(messageId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<MessageRecord> searchMessageRecordList(MessageRecord messageRecord) {
        return corgiSystemMessageMapper.searchMessageRecord(messageRecord, 0, 1000);
    }

    @Override
    public String addMessageRecord(MessageRecord messageRecord) {
        corgiSystemMessageMapper.addMessageRecord(messageRecord);
        return messageRecord.getId();
    }

    @Override
    public void updateMessageRecord(MessageRecord messageRecord) {
        corgiSystemMessageMapper.updateMessageRecord(messageRecord);
    }

    @Override
    public MessageRecordPage searchMessageRecord(MessageRecord messageRecord, Integer page, Integer pageSize) {
        List<MessageRecord> recordList = corgiSystemMessageMapper.searchMessageRecord(messageRecord, (page - 1) * pageSize, pageSize);
        MessageRecordPage recordPage = new MessageRecordPage();
        recordPage.setRecordList(recordList);
        recordPage.setPage(page);
        recordPage.setTotal(corgiSystemMessageMapper.countMessageRecord(messageRecord));
        for (MessageRecord record : recordList) {
            SystemMessage systemMessage = corgiSystemMessageMapper.getSystemMessageById(record.getMessageId());
            record.setMessage(systemMessage);
        }
        return recordPage;
    }
}
