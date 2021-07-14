package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfluencerApply implements Serializable {
    private Integer id;
    private String corgiNickname;
    private String wechatNickname;
    private String height;
    private String weight;
    private String age;
    private String city;
    private String pic1;
    private String pic2;
    private String tiktok;
    private String instagram;
    private String weibo;
    private String snapshot1;
    private String desc1;
    private String snapshot2;
    private String desc2;
    private String source;
    private String detail;
    private String ctime;
}
