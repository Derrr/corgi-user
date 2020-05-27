package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRecord implements Serializable{
    private String id;
    private String messageId;
    private String userId;
    private String nickname;
    private String status;
    private String reason;
    private String ctime;
}
