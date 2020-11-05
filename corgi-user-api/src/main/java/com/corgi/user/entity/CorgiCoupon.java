package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiCoupon implements Serializable{
    private Integer id;
    private String detail;
    private String status;
    private String barId;
    private String ctime;
}
