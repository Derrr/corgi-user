package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserMatchRemain implements Serializable {
    private String userId;
    private String tradeNo;
    private Integer remain;
}
