package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class BarReservation implements Serializable {
    private String id;
    private String barId;
    private String userId;
    private String name;
    private String date;
    private String contact;
    private String people;
    private String remark;
    private String tradeNo;
    private String merchId;
    private String status;
}
