package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiDate implements Serializable {
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String EMPTY = "empty";

    private Integer id;
    private String userId;
    private String type;
    private String endTime;
    private String budget = "0";
    private String address;
    private String ctime;
    private String status;
    private String detail;
    private String city;
    private Integer applyId;
}
