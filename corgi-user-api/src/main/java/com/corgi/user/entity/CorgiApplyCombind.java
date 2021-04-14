package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CorgiApplyCombind implements Serializable {
    private Integer count;
    private String startTime;
    private List<String> avatars;
}
