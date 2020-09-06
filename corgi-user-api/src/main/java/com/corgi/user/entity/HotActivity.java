package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class HotActivity implements Serializable {
    public static String STATUS_ENABLE = "1";
    public static String STATUS_DISABLE = "0";

    private String id;
    private String barId;
    private String activityName;
    private String activityId;
    private String startTime;
    private String endTime;
    private String status;
    private String type;
    private String city;
    private Integer order;
}
