package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserSignUp implements Serializable {
    public UserSignUp() {
        super();
    }

    public UserSignUp(String userId, String activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    public static final int AGREE = 1;
    public static final int REFUSE = 2;

    private String userId;
    private String activityId;
    private int status;
}
