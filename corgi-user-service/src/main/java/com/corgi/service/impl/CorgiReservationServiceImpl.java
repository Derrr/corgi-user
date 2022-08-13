package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiReservationMapper;
import com.corgi.user.api.*;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiReserveService.class)
@Slf4j
@Component
public class CorgiReservationServiceImpl implements CorgiReserveService {

    @Autowired
    private CorgiReservationMapper corgiReservationMapper;

    @Override
    public String addReservation(BarReservation reservation) {
        corgiReservationMapper.addBarReservation(reservation);
        return reservation.getId();
    }

    @Override
    public void updateReservation(BarReservation reservation) {
        corgiReservationMapper.updateBarReservation(reservation);
    }

    @Override
    public List<BarReservation> listAllReservation(BarReservation reservation, Integer start, Integer size) {
        return corgiReservationMapper.getAllReservations(reservation, start, size);
    }

    @Override
    public Integer countReservation(BarReservation reservation) {
        return corgiReservationMapper.countAllReservations(reservation);
    }
}
