package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserInterest implements Serializable{
    private String userId;
    private String name;
    private String category;
}
