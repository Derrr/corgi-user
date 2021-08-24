package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityLike implements Serializable {
    private String activityId;
    private String ctime;
    private String userId;
    private String timeShow;

    private String likeUserId;
    private String likeUserName;
    private String likeUserAvatar;

    private Integer isFollow;

    private String type = "real";
}
