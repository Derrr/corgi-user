package com.corgi.mapper;

import com.corgi.user.entity.CorgiDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiDateMapper {

    void addCorgiDate(@Param("date") CorgiDate date);

    List<CorgiDate> searchDate(@Param("date") CorgiDate date);

    CorgiDate getDateById(@Param("id") Integer id);

    void updateCorgiDate(@Param("date") CorgiDate date);
}
