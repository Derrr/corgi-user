package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MatchQuery implements Serializable {
    private String userId;
    private String status;
    private String query;
}
