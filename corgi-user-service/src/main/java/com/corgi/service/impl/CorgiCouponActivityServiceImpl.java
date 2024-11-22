package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCouponActivityMapper;
import com.corgi.user.api.CorgiCouponActivityService;
import com.corgi.user.entity.CouponActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiCouponActivityService.class)
@Slf4j
@Component
public class CorgiCouponActivityServiceImpl implements CorgiCouponActivityService {
    @Autowired
    private CorgiCouponActivityMapper couponActivityMapper;

    @Override
    public List<CouponActivity> getCouponList(CouponActivity couponActivity, Integer page, Integer pageSize) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -60);
        couponActivity.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
        return couponActivityMapper.getCouponList(couponActivity, (page - 1) * pageSize, pageSize);
    }

    @Override
    public CouponActivity getCouponActivity(String couponId) {
        return couponActivityMapper.getCoupon(couponId);
    }

    @Override
    public Integer countCoupon(CouponActivity couponActivity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -60);
        couponActivity.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
        return couponActivityMapper.countCoupon(couponActivity);
    }

    @Override
    public void updateCouponActivity(CouponActivity couponActivity) {
        couponActivityMapper.updateCouponActivity(couponActivity);
    }

    @Override
    public void expireCouponActivity(CouponActivity couponActivity) {
        couponActivity.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        couponActivityMapper.expireCouponActivity(couponActivity);
    }

    @Override
    public void addCouponActivity(CouponActivity couponActivity) {
        couponActivityMapper.addCouponActivity(couponActivity);
    }
}
