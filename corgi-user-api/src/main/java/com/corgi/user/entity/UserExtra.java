package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserExtra implements Serializable {
    private String xp = "[]";
    private String income = "";
    private String profession = "";
    private String education = "";
    private String aim = "";
    private String interests = "[]";
    private String tags = "[]";
}
