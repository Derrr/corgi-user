package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SystemMessage implements Serializable{
    public static String STATUS_CREATED = "created";
    public static String STATUS_DISABLED = "disabled";
    public static String STATUS_SENT = "sent";
    public static String STATUS_SENDING = "sending";

    private String id;
    private String title;
    private String content;
    private String status;
    private Long sentTime;
    private String ctime;
    private String uptime;
    private String from;

    private List<MessageRule> rules;
}
