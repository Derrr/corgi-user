package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityView implements Serializable {

    private Integer id;
    private String userId;
    private String activityId;
    private Integer viewCount;

}
