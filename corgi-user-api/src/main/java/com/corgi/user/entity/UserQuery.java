package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserQuery implements Serializable {
    private String userId;
    private Double lat;
    private Double lng;
    private Double range;
    private List<String> relation;
    private List<String> dateStatus;
    private String nickname;
    private List<String> role;
    private List<String> group;
    private Integer startHeight;
    private Integer endHeight;
    private Integer startAge;
    private Integer endAge;
    private Integer startWeight;
    private Integer endWeight;
    private String follow;
    private String type;
    private Integer limit;
    private String city;
    private String result;
    private List<String> income;
    private List<String> profession;
    private List<String> education;
    private List<String> interests;
    private List<String> tags;
    private List<String> aim;
    private List<String> xp;
}
