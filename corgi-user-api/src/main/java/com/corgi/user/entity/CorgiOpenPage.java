package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiOpenPage implements Serializable {
    public static String STATUS_ENABLE = "1";
    public static String STATUS_DISABLE = "0";

    private Integer id;
    private String title;
    private String picUrl;
    private String url;
    private String urlType;
    private String duration;
    private String startTime;
    private String endTime;
    private String picType = "image";
    private String city;
    private String province;
    private String status;
}
