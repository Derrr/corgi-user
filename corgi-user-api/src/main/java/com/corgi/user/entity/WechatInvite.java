package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatInvite implements Serializable {
    private String userId;
    private String wechatId;
    private String wechatName;
    private String headimgurl;
    private String ctime;
}
