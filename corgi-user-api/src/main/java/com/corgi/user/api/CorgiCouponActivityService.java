package com.corgi.user.api;

import com.corgi.user.entity.CouponActivity;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCouponActivityService {
    List<CouponActivity> getCouponList(CouponActivity couponActivity, Integer page, Integer pageSize);

    CouponActivity getCouponActivity(String couponId);

    void updateCouponActivity(CouponActivity couponActivity);

    void expireCouponActivity(CouponActivity couponActivity);

    void addCouponActivity(CouponActivity couponActivity);
}
