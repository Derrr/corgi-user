package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiDate implements Serializable {
    private Integer id;
    private String userId;
    private String takenUser;
    private String type;
    private String payType;
    private String endTime;
    private String ctime;
    private String status;
}
