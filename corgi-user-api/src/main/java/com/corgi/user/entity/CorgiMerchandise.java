package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiMerchandise implements Serializable {
    public static final String SUBSCRIBE = "subscribe";
    public static final String ACTIVITY = "activity";

    private String id;
    private String title;
    private String content;
    private Double price;
    private String type;
    private String discount;
    private String disReason;
    private String prePrice;
    private String status;
}
