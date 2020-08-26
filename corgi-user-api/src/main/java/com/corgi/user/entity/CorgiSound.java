package com.corgi.user.entity;

import lombok.Data;

/**
 * @author tairanliu
 */
@Data
public class CorgiSound {
    public static String NORMAL = "normal";
    public static String NEED_CHECK = "check";
    public static String FAIL = "failed";
    private String id;
    private String dataId;
    private String soundUrl;
    private String status;
    private String result;
    private String userId;
}
