package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBasic implements Serializable {
    private String userId;
    private String avatar;
    private String nickname;
    private String ctime;
}
