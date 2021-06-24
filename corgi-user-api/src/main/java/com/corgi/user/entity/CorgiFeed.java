package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiFeed implements Serializable {
    private Integer id;
    private String feed;
    private String userId;
    private String feedUserId;
    private String source;
    private String ctime;

    public boolean hasValue() {
        return feed != null && userId != null && feedUserId != null;
    }
}
