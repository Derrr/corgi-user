package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserDetail implements Serializable {
    public static final String NO_FACE = "no_face";
    public static final String VERIFIED = "verified";

    private String userId;
    private String telNo;
    private String imId;
    private String nickname;
    private String checkNickname;
    private String nicknameDataId;
    private String avatar;
    private String avatarDataId;
    private String avatarStatus;
    private String avatarCheckStatus;
    private int height;
    private int weight;
    private String desc;
    private String checkDesc;
    private String role;
    private String hideRole;
    private String city;
    private String character;
    private String group;
    private String hideGroup;
    private String hidePreferGroup;
    private String birthday;
    private String con;
    private String relation;
    private Long time;
    private Double lat;
    private Double lng;
    private String checkStatus;
    private Double match;
    private String version;
    private CorgiDate date;

    List<String> preferGroup;

    List<UserPic> userPics;

    List<String> tags;

    List<UserInterest> interests;

    public String getNatureCharacter() {
        if (this.character == null || this.character.length() < 4) {
            return "";
        }
        return character.substring(0, 4);
    }


    public String getAvatar() {
        if ("check".equals(this.getAvatarCheckStatus()) && this.avatar != null && !this.avatar.contains("?x-oss-process")) {
            return this.avatar + "?x-oss-process=style/mask";
        }
        if ("check".equals(this.getAvatarCheckStatus())) {
            return this.avatar;
        }
        if (this.avatar != null) {
            return this.avatar.split("\\?")[0];
        }
        return avatar;
    }
}
