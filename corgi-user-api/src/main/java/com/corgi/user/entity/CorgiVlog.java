package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiVlog implements Serializable {
    interface TYPE {
        String USER = "user";
        String ACTIVITY = "activity";
        String GOODS = "goods";
    }

    interface STATUS {
        String UNCHECK = "uncheck";
        String PASS = "pass";
        String FAILED = "failed";
    }

    private Integer id;
    private String activityId;
    private String userId;
    private String ctime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private String type;
    private String status;

}
