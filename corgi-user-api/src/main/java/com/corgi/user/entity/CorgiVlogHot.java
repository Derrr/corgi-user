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
public class CorgiVlogHot implements Serializable {
    public interface TYPE {
        String AUTO = "auto";
        String MANUAL = "manual";
    }

    public interface STATUS {
        String CLOSE = "close";
        String OPEN = "open";
    }

    private Integer id;
    private String activityId;
    private String ctime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer expectView;
    private String type;
    private String status;
    private String userId;

}
