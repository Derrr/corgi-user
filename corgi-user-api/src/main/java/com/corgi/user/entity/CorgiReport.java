package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CorgiReport implements Serializable{
    public static final String USER = "user";
    public static final String ACTIVITY = "activity";

    private String id;
    private String reportUserId;
    private String reportUserName;
    private String accuseId;
    private String accuseType;
    private String accuseName;
    private String desc;
    private String reason;
    private String reportStatus;
    private String ctime;
    private String uptime;
    private Integer accuseTime;
    private String accuseUserId;
    private String result;

    List<String> pics;
}
