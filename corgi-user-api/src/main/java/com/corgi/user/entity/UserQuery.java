package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserQuery implements Serializable{
    private String userId;
    private Double lat;
    private Double lng;
    private Double range;
    private List<String> role;
    private List<String> group;
    private Integer startHeight;
    private Integer endHeight;
    private Integer startWeight;
    private Integer endWeight;
}
