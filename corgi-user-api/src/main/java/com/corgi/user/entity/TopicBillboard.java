package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopicBillboard implements Serializable {
    private String activityId;
    private String topic;
    private Integer status;
    private Integer order;
    private String ctime;
}
