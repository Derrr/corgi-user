package com.corgi.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorgiOrder implements Serializable {
    public interface PAY_TYPE {
        String WX = "wx";
        String ALIPAY = "alipay";
        String IN_APP = "inApp";
        String PAY = "pay";
        String WITHDRAW = "withdraw";
        String COUPON = "coupon";
    }

    public interface STATUS {
        String CREATED = "created";
        String PROCESSING = "processing";
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
    private String desc;
    private String orderId;
    private String packageName;
    private String status;
    private String ctime;
}
