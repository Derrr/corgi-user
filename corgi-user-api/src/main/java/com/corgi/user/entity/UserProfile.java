package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author tairanliu
 */
@Data
public class UserProfile implements Serializable {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String userId;
    private String nickname;
    private String checkNickname;
    private String desc;
    private String checkDesc;
    private String telNo;
    private String imId;
    private String avatar;
    private String avatarStatus;
    private String avatarCheckStatus;
    private String role;
    private String hideRole;
    private String group;
    private String hideGroup;
    private String hidePreferGroup;
    private String con;
    private String relation;
    private Double lat;
    private Double lng;
    private Long time;
    private Double match;
    private int signUpStatus;
    private Integer isFollowed;
    private String character;
    private String checkStatus;
    private String isRead;
    private String createTime;
    private String followTime;
    private Integer activityCount;
    private String locateType;
    private Integer weight;
    private Integer height;
    private String birthday;
    private String city;
    List<UserPic> pics;
    CorgiSound sounds;

    public String getFollowTime() {
        if (followTime != null && !"".equals(followTime)) {
            try {
                Date date = sdf.parse(followTime);
                return date.getTime() + "";
            } catch (Exception e) {

            }
        }
        return followTime;
    }

    public String getAvatar() {
        return this.avatar != null ? this.avatar.split("\\?")[0] : this.avatar;
    }

}
