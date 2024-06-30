package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiBehaviorReq implements Serializable {
    public enum PayType {
        subscribe, activity, location, match;
    }

    public enum ActivityType {
        creator("creator", "creator_id"),
        //like("like", "like_user_id"),
        share("share", "share_user_id"),
        comment("comment", "comment_user_id"),
        ;

        public String getType() {
            return type;
        }

        public String getUser() {
            return user;
        }

        private String type;
        private String user;

        ActivityType(String type, String user) {
            this.type = type;
            this.user = user;
        }

    }

    private String startTime;
    private String endTime;
    private String type;

    public boolean isPayType() {
        for (PayType payType : PayType.values()) {
            if (payType.toString().equals(this.type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivityType() {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getType().equals(this.type)) {
                return true;
            }
        }
        return false;
    }

}
