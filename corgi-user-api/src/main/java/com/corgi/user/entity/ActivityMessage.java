package com.corgi.user.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ActivityMessage implements Serializable {
    public static final String COMMENT = "1";
    public static final String LIKE = "2";


    private String messageType;
    private String content;
    private String activityId;
    private String toUserId;
    private String fromUserId;
    private String fromUserName;
    private String fromUserAvatar;
    private Long time;
}
