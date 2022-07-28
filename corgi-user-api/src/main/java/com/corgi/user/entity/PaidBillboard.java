package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaidBillboard implements Serializable {
    public static final String CREATED = "created";
    public static final String PAID = "paid";
    public static final String PASS = "pass";
    public static final String FAIL = "fail";

    private String id;
    private String activityId;
    private String userId;
    private String payId;
    private String date;
    private String status;
    private String tradeNo;
}
