package com.corgi.support;

import com.corgi.user.entity.UserPosition;
import com.corgi.user.entity.UserQuery;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author tairanliu
 */
@Data
public class UserQuerySupporter {
    public static Double EARTH_RADIUS = 6371.393;
    public static Double INFINITY_SMALL = 0.00001;
    private UserQuery userQuery;

    private String userId;
    private Double beginLat;
    private Double endLat;
    private Double beginLng;
    private Double endLng;

    private Integer startHeight;
    private Integer endHeight;

    private Integer startWeight;
    private Integer endWeight;

    private String role;
    private String group;
    private String relation;
    private String follow = "";
    private String type;
    private double lat;
    private double lng;

    private Integer limit = 0;

    public UserQuerySupporter(UserQuery userQuery) {
        this.lat = userQuery.getLat();
        this.lng = userQuery.getLng();

        this.userId = userQuery.getUserId();
        this.userQuery = userQuery;
        Double range = userQuery.getRange();

        Double dtheta = (range * 180) / (EARTH_RADIUS * Math.PI);
        this.beginLat = userQuery.getLat() - dtheta;
        this.endLat = userQuery.getLat() + dtheta;

        Double dphi = (range * 180) / (EARTH_RADIUS * Math.PI * (Math.cos(Math.toRadians(userQuery.getLat())) + INFINITY_SMALL));
        this.beginLng = userQuery.getLng() - dphi;
        this.endLng = userQuery.getLng() + dphi;

        if (userQuery.getStartHeight() != null) {
            this.startHeight = userQuery.getStartHeight();
        }
        if (userQuery.getEndHeight() != null) {
            this.endHeight = userQuery.getEndHeight();
        }

        if (userQuery.getStartWeight() != null) {
            this.startWeight = userQuery.getStartWeight();
        }
        if (userQuery.getEndWeight() != null) {
            this.endWeight = userQuery.getEndWeight();
        }

        if (!StringUtils.isEmpty(userQuery.getFollow())) {
            this.follow = userQuery.getFollow();
        }

        if (!CollectionUtils.isEmpty(userQuery.getRelation())) {
            StringBuilder sb = new StringBuilder("('");
            for (String relation : userQuery.getRelation()) {
                sb.append(relation + "','");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
            this.relation = sb.toString();
        }

        if (!CollectionUtils.isEmpty(userQuery.getRole())) {
            StringBuilder sb = new StringBuilder("('");
            for (String role : userQuery.getRole()) {
                sb.append(role + "','");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
            this.role = sb.toString();
        }

        if (!CollectionUtils.isEmpty(userQuery.getGroup())) {
            StringBuilder sb = new StringBuilder("('");
            for (String group : userQuery.getGroup()) {
                sb.append(group + "','");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
            this.group = sb.toString();
        }
        this.type = userQuery.getType();

        if (userQuery.getLimit() != null) {
            this.limit = userQuery.getLimit();
        }
    }
}
