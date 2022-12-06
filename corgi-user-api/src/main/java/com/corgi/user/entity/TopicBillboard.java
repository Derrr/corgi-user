package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopicBillboard implements Serializable {
    public static final String PREFIX = "topic_most_popular_";
    private String activityId;
    private String topic;
    private Integer status;
    private Integer order;
    private String ctime;
}
