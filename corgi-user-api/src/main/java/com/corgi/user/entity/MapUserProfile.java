package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class MapUserProfile implements Serializable {
    List<UserProfile> users;
    List<String> noDateUserIds;
    List<String> userIds;
}
