package com.corgi.user.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author tairanliu
 */
@Data
public class UserEvaluation {
    public static final String TYPE_FRIEND = "friend";
    public static final String TYPE_DATE = "date";

    private Integer id;
    private String userId;
    private String evaluatorId;
    private String evaluatorName;
    private String evaluatorAvatar;
    private String status;
    private Timestamp ctime;
    private String tag;
    private String type;
    private String applyId;
    private double score;

    public String getTag() {
        if (this.tag != null) {
            return tag.trim();
        }
        return null;
    }

    public String getCtime() {
        if (this.ctime != null) {
            return ctime.getTime() + "";
        }
        return "0";
    }

}
