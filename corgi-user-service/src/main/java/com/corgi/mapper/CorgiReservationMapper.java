package com.corgi.mapper;

import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiReservationMapper {
    /**
     * 获取reserve
     *
     * @param barReservation
     * @param limit
     * @return
     */
    List<BarReservation> getAllReservations(@Param("reserve") BarReservation barReservation, @Param("start") Integer start, @Param("limit") Integer limit);

    Integer countAllReservations(@Param("reserve") BarReservation barReservation);

    void addBarReservation(@Param("reserve")BarReservation barReservation);

    void updateBarReservation(@Param("reserve")BarReservation barReservation);
}
