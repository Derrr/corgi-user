package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityBillboard implements Serializable {
    private String activityId;
    private String lastTime;
    private String ctime;
}
