package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class BarProfile implements Serializable {
    public static String STATUS_ENABLE = "1";
    public static String STATUS_DISABLE = "0";

    private String barId;
    private String barName;
    private String logo;
    private String address;
    private Integer range;
    private Double lat;
    private Double lng;
    private String tel;
    private String status;
    private String account;
    private String password;
    private String startTime;
    private String endTime;
    private String detail;
    private String spending;
    private Long heat;
    private String type;
    private String city;
    private String ctime;
    private Long relActivityCount;
    private String cuid;
    private String qrCode;
    private String picUrl;

    Integer activityCount;
    List<BarPic> barPics;

    private String video;

    public String getType() {
        if (barId == null) {
            return "";
        }
        if (this.barId.startsWith("B")) {
            return "platform";
        }
        if (this.barId.startsWith("C")) {
            return "user";
        }
        return "";
    }
}
