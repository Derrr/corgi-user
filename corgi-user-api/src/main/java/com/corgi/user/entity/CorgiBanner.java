package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiBanner implements Serializable {
    public static String STATUS_ENABLE = "1";
    public static String STATUS_DISABLE = "0";

    public static String TYPE_RECOMMEND = "recommend";
    public static String TYPE_ACTIVITY = "activity";

    private Integer id;
    private String title;
    private String picUrl;
    private String url;
    private String urlType;
    private String type;
    private Integer order;
    private String status;
}
