package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserProfile implements Serializable {
    private String userId;
    private String nickname;
    private String avatar;
    private String role;
    private String group;
    private String con;
    private Double lat;
    private Double lng;
    private Long time;
    private Double match;
}
