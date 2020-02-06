package com.corgi.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tairanliu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTrace {
    public static final String STAYING = "staying";
    public static final String CHANGED = "skipped";

    private String id;
    private String userId;
    private String type;
    private String nextType;
    private long stayCount;
    private String stayStatus;
    private String ctime;
    private long uptime;
}
