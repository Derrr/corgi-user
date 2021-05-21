package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiDateApply implements Serializable {
    public static final String APPLY = "apply";
    public static final String AGREE = "agree";
    public static final String CANCEL = "cancel";

    private Integer id;
    private String applyUserId;
    private String approvalUserId;
    private String dateId;
    private String ctime;
    private String uptime;
    private String status;
    private String detail;
    private String endTime;
    private String address;
    private Double lat;
    private Double lng;
    private String operator;
    private String type;
    private String result;
    private String budget;
    private UserDetail userInfo;
    private String progress;
}
