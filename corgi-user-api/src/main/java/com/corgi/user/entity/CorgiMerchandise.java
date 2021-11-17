package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiMerchandise implements Serializable {
    public static final String SUBSCRIBE = "subscribe";

    private String id;
    private String title;
    private String content;
    private Double price;
    private String type;
    private String status;
}
