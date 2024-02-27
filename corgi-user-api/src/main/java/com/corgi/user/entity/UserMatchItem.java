package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserMatchItem implements Serializable{
    private String userId;
    private String avatar;
    private String dateStatus;
    private String timeShow;
    private String distance;
    private String avatarStatus;
    private String nickname;
    private UserQuery query;
}
