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
public class CorgiUserGoods implements Serializable {
    public interface GOODS_TYPE {
        String SUBSCRIBE = "subscribe";
        String ACTIVITY = "activity";
    }
    public interface CURRENCY{
        String CNY = "cny";
    }

    private String id;
    private String tradeNo;
    private String goodsId;
    private String goodsType;
    private String userId;
    private String traderId;
    private String currency;
    private String merchId;
    private String marketId;
    private Double price;
    private String desc;
    private String status;
    private Integer start;
    private Integer size;
    private String ctime;
    private String uptime;
}
