package com.corgi.user.entity;

import com.corgi.entity.CorgiPic;
import lombok.Data;

/**
 * @author tairanliu
 */
@Data
public class UserVideo {
    private String userId;
    private String videoUrl;
    private String videoId;
}
