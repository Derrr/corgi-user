package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MessageRecordPage implements Serializable {
    private Integer total;
    private Integer page;
    List<MessageRecord> recordList;
}
