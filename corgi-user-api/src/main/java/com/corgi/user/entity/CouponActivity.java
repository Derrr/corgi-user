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
public class CouponActivity implements Serializable {
    private Integer id;
    private String userId;
    private Double value;
    private String status;
    private String ctime;
    private String expireDate;
    private String usedActivity;
}
