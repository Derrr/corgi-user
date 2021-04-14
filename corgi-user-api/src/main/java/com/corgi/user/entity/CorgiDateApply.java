package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiDateApply implements Serializable {
    public static final String APPLY = "apply";
    public static final String AGREE = "agree";
    public static final String CANCEL = "cancel";
    public static final String REJECT = "reject";

    private Integer id;
    private String applyUserId;
    private String approvalUserId;
    private String dateId;
    private String ctime;
    private String uptime;
    private String status;
    private String result;
    private CorgiDate dateDetail;
    private UserDetail userInfo;
}
