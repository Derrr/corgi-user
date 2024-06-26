package com.corgi.mapper;

import com.corgi.user.entity.CouponActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCouponActivityMapper {

    List<CouponActivity> getCouponList(@Param("coupon")CouponActivity couponActivity,
                                       @Param("start")Integer start, @Param("size")Integer size);

    CouponActivity getCoupon(@Param("couponId")String couponId);

    void updateCouponActivity(@Param("coupon")CouponActivity couponActivity);

    void expireCouponActivity(@Param("coupon")CouponActivity couponActivity);

    void addCouponActivity(@Param("coupon")CouponActivity couponActivity);

}
