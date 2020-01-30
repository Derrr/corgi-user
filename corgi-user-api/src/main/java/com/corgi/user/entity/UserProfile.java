package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserProfile implements Serializable {
    private String userId;
    private String nickname;
    private String checkNickname;
    private String desc;
    private String checkDesc;
    private String telNo;
    private String imId;
    private String avatar;
    private String avatarStatus;
    private String role;
    private String group;
    private String con;
    private Double lat;
    private Double lng;
    private Long time;
    private Double match;
    private int signUpStatus;
    private Integer isFollowed;
    List<UserPic> pics;
}
