package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DateType implements Serializable {
    private Integer id;
    private String type;
    private String content;
}
