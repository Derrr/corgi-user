package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityComment implements Serializable {
    private String commentId;
    private String parentCommentId;
    private String activityId;
    private String userId;
    private String content;
    private String ctime;

    private String commentUserId;
    private String commentUserName;
    private String commentUserAvatar;

    private String replyUserId;
    private String replyUserName;
    private String replyUserAvatar;
}
