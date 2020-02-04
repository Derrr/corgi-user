package com.corgi.mapper;

import com.corgi.entity.CorgiStatistic;
import com.corgi.entity.CorgiTopic;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiStatisticMapper {

    /**
     * 添加统计
     *
     * @param table
     * @param date
     * @param count
     */
    void addCount(@Param("table") String table, @Param("date") String date, @Param("count") Long count);

    /**
     * 删除统计
     *
     * @param table
     * @param date
     */
    void deleteCount(@Param("table") String table, @Param("date") String date);


    /**
     * 获取统计
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    List<CorgiStatistic> getCount(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 总计统计
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    long sumCount(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 创建活跃时间
     *
     * @param date
     */
    void initMap(@Param("date") String date, @Param("table") String table);

    /**
     * 更新数据
     *
     * @param table
     * @param date
     * @param key
     * @param count
     */
    void updateMap(@Param("table") String table, @Param("date") String date, @Param("key") String key, @Param("count") Long count);

    /**
     * 获取时间段列表
     *
     * @param beginDate
     * @param endDate
     * @param table
     * @return
     */
    List<HashMap> getMap(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取所有时间段List
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    List<CorgiStatistic> getList(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 添加List
     *
     * @param table
     * @param date
     * @param name
     * @param count
     */
    void addList(@Param("table") String table, @Param("date") String date, @Param("name") String name, @Param("count") Long count);

    /**
     * 删除List
     *
     * @param table
     * @param name
     * @param date
     */
    void deleteList(@Param("table") String table, @Param("date") String date, @Param("name") String name);

    /**
     * 更新List
     *
     * @param table
     * @param date
     * @param name
     * @param count
     */
    void updateList(@Param("table") String table, @Param("date") String date, @Param("name") String name, @Param("count") Long count);
}
