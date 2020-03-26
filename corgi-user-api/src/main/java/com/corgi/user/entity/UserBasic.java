package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserBasic implements Serializable {
    private String userId;
    private String nickname;
    private String ctime;
    List<UserPic> userPics;
}
