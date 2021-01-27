package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Billboard implements Serializable {
    private String userId;
    private String nickname;
    private String avatar;

    private int count;
    private String countType;
    private String date;
    private String lastDate;
}
