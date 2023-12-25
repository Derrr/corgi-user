package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CorgiCoupon implements Serializable{
    private Integer id;
    private String detail;
    private String content;
    private String autoContent;
    private String background;
    private String time;
    private String address;
    private String title;
    private String price;
    private String barId;
    private String ctime;

    private List<String> pics;
}
