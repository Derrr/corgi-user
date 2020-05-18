package com.corgi.user.entity;

import lombok.Data;

@Data
public class ActivityLike {
    private String activityId;
    private String ctime;
    private String userId;

    private String likeUserId;
    private String likeUserName;
    private String likeUserAvatar;
}
