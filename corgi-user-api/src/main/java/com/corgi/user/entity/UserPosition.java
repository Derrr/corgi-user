package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPosition implements Serializable {
    private String userId;
    private String version;
    private Double lat;
    private Double lng;
    private String city;
    private String province;

    private Double realLat;
    private Double realLng;
    private String locateType;

    private Long uptime;
    private String channel;
    private Long onlineTime;

    public String getLocateType() {
        if (locateType == null || "".equals(locateType)) {
            return "0";
        }
        return locateType;
    }

}
