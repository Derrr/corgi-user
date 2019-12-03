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
}
