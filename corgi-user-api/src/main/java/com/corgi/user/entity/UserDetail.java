package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserDetail implements Serializable{
    private String userId;
    private String telNo;
    private String imId;
    private String nickname;
    private String checkNickname;
    private String nicknameDataId;
    private String avatar;
    private String avatarDataId;
    private String avatarStatus;
    private int height;
    private int weight;
    private String desc;
    private String checkDesc;
    private String role;
    private String city;
    private String character;
    private String group;
    private String birthday;
    private String con;

    List<String> preferGroup;

    List<UserPic> userPics;

    List<String> tags;

    List<UserInterest> interests;

    public String getNatureCharacter() {
        if (this.character == null) {
            return "";
        }
        return character.substring(0, 4);
    }
}
