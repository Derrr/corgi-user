package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPic implements Serializable{
    private String picId;
    private String userId;
    private String picUrl;
}
