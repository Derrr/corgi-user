package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCouponMapper;
import com.corgi.user.api.CorgiCouponService;
import com.corgi.user.entity.CorgiCoupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<CorgiCoupon> getCoupon(String barId, String status) {
        return corgiCouponMapper.getBarCoupon(barId, status);
    }

    @Override
    public void addCoupon(CorgiCoupon corgiCoupon) {
        corgiCouponMapper.insertCoupon(corgiCoupon);
    }

    @Override
    public void updateCoupon(CorgiCoupon corgiCoupon) {
        corgiCouponMapper.updateCoupon(corgiCoupon);
    }

    @Override
    public void deleteCoupon(Integer id, String barId) {
        corgiCouponMapper.deleteCoupon(id, barId);
        corgiCouponMapper.deleteActivityCouponId(id, barId);
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
