package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiContentStatistics implements Serializable {
    private Integer totalCount;
    private String contentId;
}
