package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfluencerApply implements Serializable {
    private Integer id;
    private String userId;
    private String nickname;
    private String avatar;
    private String wechat;
    private String status;
    private String ctime;
    private String uptime;
}
