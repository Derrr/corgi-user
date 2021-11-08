package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiHashtag implements Serializable {
    public static final String NEW = "created";
    public static final String RELEASE = "release";
    public static final String OFF = "off";
    String picUrl;
    String hastagId;
    String hastagName;
    String status;
    String content;
    String ctime;
    String uptime;
    String cuid;
    String opuid;
    Integer order;
    String url;
    String urlType;
}
