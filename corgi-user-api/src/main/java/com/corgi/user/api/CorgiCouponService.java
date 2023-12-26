package com.corgi.user.api;

import com.corgi.user.entity.BarProfile;
import com.corgi.user.entity.CorgiCoupon;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCouponService {
    List<CorgiCoupon> getCoupon(String barId, Integer page, Integer size);

    Integer countCoupon(String barId);

    void addCoupon(CorgiCoupon corgiCoupon);

    void updateCoupon(CorgiCoupon corgiCoupon);

    void deleteCoupon(Integer id, String barId);

    List<CorgiCoupon> getActivityCoupon(String activityId, String status);

    void addActivityCoupon(String activityId, Integer couponId);

    void deleteActivityCoupon(String activityId);
}
