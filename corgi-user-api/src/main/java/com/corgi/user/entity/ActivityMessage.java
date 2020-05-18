package com.corgi.user.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityMessage {
    public static final String COMMENT = "1";
    public static final String LIKE = "2";


    private String messageType;
    private String activityId;
    private String toUserId;
    private String fromUserId;
    private String fromUserName;
    private String fromUserAvatar;
    private Long time;
}
