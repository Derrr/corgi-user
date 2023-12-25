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
     */
    void deleteCoupon(@Param("id") Integer id);

    /**
     * 列出优惠券
     *
     * @param start
     * @param size
     */
    List<CorgiCoupon> listCoupon(@Param("barId")String barId, @Param("start")Integer start, @Param("size")Integer size);

    /**
     * 统计优惠券
     *
     */
    Integer countCoupon(@Param("barId")String barId);

    /**
     * 获取商户优惠券
     *
     * @param barId
     * @return
     */
    List<CorgiCoupon> getBarCoupon(@Param("barId") String barId);

    /**
     * 获取优惠券图片
     * @param id
     * @return
     */
    List<String> getCouponPic(@Param("couponId")Integer id);

    /**
     * 添加优惠券图片
     * @param id
     */
    void addCouponPic(@Param("couponId")Integer id, @Param("url") String imageUrl);

    /**
     * 删除优惠券图片
     * @param id
     */
    void deleteCouponPic(@Param("couponId")Integer id);

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
