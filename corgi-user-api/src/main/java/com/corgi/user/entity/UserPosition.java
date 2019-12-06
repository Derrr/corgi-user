package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPosition implements Serializable{
    private String userId;
    private Double lat;
    private Double lng;
}
