package com.corgi.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMessage implements Serializable {
    public static final String COMMENT = "1";
    public static final String LIKE = "2";
    private String messageType;
    private String content;
    private String activityId;
    private String text;
    private String toUserId;
    private String fromUserId;
    private String fromUserName;
    private String fromUserAvatar;
    private String commentId;
    private String isRead;
    private String status;
    private String activityPic;
    private Long time;
}
