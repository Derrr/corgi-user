package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPosition implements Serializable{
    private String userId;
    private String version;
    private Double lat;
    private Double lng;

    private Double realLat;
    private Double realLng;
}
