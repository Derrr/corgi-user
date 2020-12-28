package com.corgi.mapper;

import com.corgi.user.entity.CorgiCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCouponMapper {
    /**
     * 添加优惠券
     *
     * @param coupon
     */
    void insertCoupon(@Param("coupon") CorgiCoupon coupon);

    /**
     * 更新优惠券
     *
     * @param coupon
     */
    void updateCoupon(@Param("coupon") CorgiCoupon coupon);

    /**
     * 删除优惠券
     *
     * @param id
     * @param barId
     */
    void deleteCoupon(@Param("id") Integer id, @Param("barId") String barId);

    /**
     * 获取商户优惠券
     *
     * @param barId
     * @param status
     * @return
     */
    List<CorgiCoupon> getBarCoupon(@Param("barId") String barId, @Param("status") String status);

    /**
     * 获取活动优惠券
     *
     * @param activityId
     * @param status
     * @return
     */
    List<CorgiCoupon> getActivityCoupon(@Param("activityId") String activityId, @Param("status") String status);

    /**
     * 删除活动优惠券
     *
     * @param activityId
     */
    void deleteActivityCoupon(@Param("activityId") String activityId);

    /**
     * 删除活动优惠券
     *
     * @param couponId
     * @param barId
     */
    void deleteActivityCouponId(@Param("couponId") Integer couponId, @Param("barId")String barId);


    /**
     * 添加活动优惠券
     * @param activityId
     * @param couponId
     */
    void addActivityCoupon(@Param("activityId") String activityId, @Param("couponId") Integer couponId);
}
