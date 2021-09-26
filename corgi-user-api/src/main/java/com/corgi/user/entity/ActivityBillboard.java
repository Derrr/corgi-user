package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityBillboard implements Serializable {
    private String activityId;
    private String userId;
    private Integer count;
    private String date;
    private Integer status;
    private Integer order;
    private String ctime;
}
