package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TlxActivity implements Serializable {
    private String id;
    private String city;
    private String shorttitle;
    private String headerImage;
    private String posterImage;
    private String meetingPoint;
    private Double days;
    private String departdate;
    private String period;
    private String status;
    private Double price;
    private String longtitle;
    private String body;
    private String tripContent;
    private String expenseDetail;
    private String note;
    private String version;
    private String hot;
    private String ctime;
    private String uptime;
}
