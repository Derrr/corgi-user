package com.corgi.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeMapper {
    void initFakeUserPool(@Param("size") Integer size, @Param("time") Long time);

    void clearFakeUserPool();

    String selectFakeUser(@Param("limit") Integer limit);

    Integer countFakeUser();
}
