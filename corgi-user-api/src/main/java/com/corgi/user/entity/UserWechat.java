package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserWechat implements Serializable {
    private String id;
    private String userId;
    private String nickname;
    private String avatar;
    private String wechat;
    private String wechatShot;
    private String reply;
    private String status;
    private String merchId;
    private String ctime;
    private String uptime;
    private String sort;
}
