package com.corgi.user.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CorgiUserMarket implements Serializable {

    private String id;
    private String merchId;
    private String goodsType;
    private String userId;
    private String sourceId;
    private String status;
    private String ctime;
    private String uptime;
}
