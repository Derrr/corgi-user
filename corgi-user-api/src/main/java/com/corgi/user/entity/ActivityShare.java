package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityShare implements Serializable {
    private String activityId;
    private String ctime;
    private String userId;

    private String shareUserId;
}
