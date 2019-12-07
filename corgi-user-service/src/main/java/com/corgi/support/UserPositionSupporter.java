package com.corgi.support;

import com.corgi.user.entity.UserPosition;
import lombok.Data;

/**
 * @author tairanliu
 */
@Data
public class UserPositionSupporter {
    public static Double EARTH_RADIUS = 6371.393;
    public static Double INFINITY_SMALL = 0.00001;
    private UserPosition userPosition;

    private Double range;
    private Double beginLat;
    private Double endLat;
    private Double beginLng;
    private Double endLng;

    public UserPositionSupporter(UserPosition userPosition, Double range) {
        this.userPosition = userPosition;
        this.range = range;

        Double dtheta = (range * 180) / (EARTH_RADIUS * Math.PI);
        this.beginLat = userPosition.getLat() - dtheta;
        this.endLat = userPosition.getLat() + dtheta;

        Double dphi = range / (EARTH_RADIUS * (Math.cos((userPosition.getLat() * Math.PI / 180.0)) + INFINITY_SMALL));
        this.beginLng = userPosition.getLng() - dphi;
        this.endLng = userPosition.getLng() + dphi;
    }
}
