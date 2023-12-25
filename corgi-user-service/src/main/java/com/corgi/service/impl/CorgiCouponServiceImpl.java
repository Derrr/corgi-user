package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCouponMapper;
import com.corgi.user.api.CorgiCouponService;
import com.corgi.user.entity.CorgiCoupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiCouponService.class)
@Slf4j
@Component
public class CorgiCouponServiceImpl implements CorgiCouponService {

    @Autowired
    private CorgiCouponMapper corgiCouponMapper;

    @Override
    public List<CorgiCoupon> getCoupon(String barId, Integer page, Integer size) {
        List<CorgiCoupon> coupons = corgiCouponMapper.listCoupon(barId, (page - 1) * size, size);
        for (CorgiCoupon coupon : coupons) {
            coupon.setPics(corgiCouponMapper.getCouponPic(coupon.getId()));
        }
        return coupons;
    }

    @Override
    public Integer countCoupon(String barId) {
        return corgiCouponMapper.countCoupon(barId);
    }

    @Override
    public void addCoupon(CorgiCoupon corgiCoupon) {
        corgiCouponMapper.insertCoupon(corgiCoupon);
        if (corgiCoupon.getPics() != null) {
            for (String pic : corgiCoupon.getPics()) {
                corgiCouponMapper.addCouponPic(corgiCoupon.getId(), pic);
            }
        }
    }

    @Override
    public void updateCoupon(CorgiCoupon corgiCoupon) {
        corgiCouponMapper.updateCoupon(corgiCoupon);
        if (corgiCoupon.getPics() != null) {
            corgiCouponMapper.deleteCouponPic(corgiCoupon.getId());
            for (String pic : corgiCoupon.getPics()) {
                corgiCouponMapper.addCouponPic(corgiCoupon.getId(), pic);
            }
        }
    }

    @Override
    public void deleteCoupon(Integer id, String barId) {
        corgiCouponMapper.deleteCoupon(id);
        corgiCouponMapper.deleteCouponPic(id);
    }

    @Override
    public List<CorgiCoupon> getActivityCoupon(String activityId, String status) {
        return corgiCouponMapper.getActivityCoupon(activityId, status);
    }

    @Override
    public void addActivityCoupon(String activityId, Integer couponId) {
        corgiCouponMapper.addActivityCoupon(activityId, couponId);
    }

    @Override
    public void deleteActivityCoupon(String activityId) {
        corgiCouponMapper.deleteActivityCoupon(activityId);
    }
}
