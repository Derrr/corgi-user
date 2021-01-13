package com.corgi.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorgiVlog implements Serializable {
    public interface TYPE {
        String USER = "user";
        String ACTIVITY = "activity";
        String GOODS = "goods";
    }

    public interface STATUS {
        String UNCHECK = "uncheck";
        String PASS = "pass";
        String FAILED = "failed";
    }

    private Integer id;
    private String activityId;
    private String userId;
    private String ctime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private String type;
    private String status;

}
