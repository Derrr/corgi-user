package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserMatch implements Serializable{
    private String userId1;
    private String userId2;
    private double match;
    private String uptime;
}
