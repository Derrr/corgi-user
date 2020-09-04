package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class PushLog implements Serializable {
    private String from;
    private String to;
    private String ctime;
}
