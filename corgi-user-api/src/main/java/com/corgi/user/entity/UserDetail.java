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
    private String nickname;
    private String avatar;
    private int height;
    private int weight;
    private String desc;
    private String role;
    private String city;
    private String character;
    private String group;
    private String birthday;
    private String con;

    List<String> preferGroup;

    List<UserPic> userPics;

    public String getNatureCharacter() {
        if (this.character == null) {
            return "";
        }
        return character.substring(0, 4);
    }
}
