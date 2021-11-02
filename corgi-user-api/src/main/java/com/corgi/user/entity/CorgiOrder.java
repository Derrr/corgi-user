package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CorgiOrder implements Serializable {
    public interface PAY_TYPE {
        String IOS = "ios";
        String ANDROID = "android";
        String APP_STORE = "appStore";
    }

    private String id;
    private String tradeNo;
    private String merchId;
    private String payType;
    private String userId;
    private String marketId;
    private String buyerId;
    private String sellerId;
    private Double payAmount;
    private String result;
    private String payTime;
    private String status;
}
