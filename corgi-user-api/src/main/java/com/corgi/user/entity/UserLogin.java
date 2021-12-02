package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserLogin implements Serializable{
    String userId;
    String telNo;
    String imId;
    String code;
    String version;
    String status;
    String newGidar;
    String newSignUp;
    String newMessage;
    String newFollowActivity;
    String unregisterDate;
    String hideVisit;
    String jwt;
    String ctime;
}
