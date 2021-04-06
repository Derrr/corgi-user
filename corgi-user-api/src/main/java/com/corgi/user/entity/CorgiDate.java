package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiDate implements Serializable {
    public static final String OPEN = "open";
    public static final String CLOSE = "close";

    private Integer id;
    private String userId;
    private String type;
    private String ctime;
    private String status;
    private String detail;
}
