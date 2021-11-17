package com.corgi.user.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CorgiOrder implements Serializable {
    public interface PAY_TYPE {
        String WX = "wx";
        String ALIPAY = "alipay";
        String APP_STORE = "appStore";
    }

    public interface STATUS {
        String CREATED = "created";
        String SUCCESS = "success";
        String CLOSE = "close";
        String FAIL = "fail";
    }

    private String id;
    private String tradeNo;
    private String merchId;
    private String merchType;
    private String payType;
    private String userId;
    private String marketId;
    private String buyerId;
    private String sellerId;
    private Double payAmount;
    private String result;
    private String payTime;
    private String status;
    private String ctime;
}
