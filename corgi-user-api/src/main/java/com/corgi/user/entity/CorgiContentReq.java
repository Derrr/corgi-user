package com.corgi.user.entity;

import lombok.Data;

@Data
public class CorgiContentReq {
    public static final String LIKE = "like";
    public static final String SHARE = "share";
    public static final String COMMENT = "comment";
    public static final String USER_LIKE ="userLike";
    public static final String FOLLOW = "follow";

    private String startTime;
    private String endTime;
    private String type;


}
