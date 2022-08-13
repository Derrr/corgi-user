package com.corgi.user.api;

import com.corgi.user.entity.BarReservation;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiReserveService {

    String addReservation(BarReservation reservation);

    void updateReservation(BarReservation reservation);

    List<BarReservation> listAllReservation(BarReservation reservation,Integer start, Integer size);

    Integer countReservation(BarReservation reservation);
}
