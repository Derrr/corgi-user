package com.corgi.user.entity;

import lombok.Data;

/**
 * @author tairanliu
 */
@Data
public class BarProfile {
    public static String STATUS_ENABLE = "1";
    public static String STATUS_DISABLE = "0";

    private String barId;
    private String barName;
    private String logo;
    private String address;
    private Integer range;
    private Double lat;
    private Double lng;
    private String tel;
    private String status;
}
