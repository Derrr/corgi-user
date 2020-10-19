package com.corgi.user.entity;

import com.corgi.entity.CorgiPic;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserVideo implements Serializable {
    private String userId;
    private String videoUrl;
    private String videoId;
}
