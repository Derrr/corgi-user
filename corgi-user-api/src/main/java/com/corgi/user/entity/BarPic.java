package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class BarPic implements Serializable {
    private String picId;
    private String picUrl;
    private String barId;
}
