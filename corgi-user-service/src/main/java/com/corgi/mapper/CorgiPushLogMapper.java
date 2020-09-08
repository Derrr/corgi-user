package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.PushLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiPushLogMapper {

    /**
     * 添加日志
     * @param pushLog
     */
    void addPushLog(@Param("log") PushLog pushLog);

    /**
     * 统计成功召回推送
     * @param date
     */
    Long countUsefulPush(@Param("date")String date);

    /**
     * 统计所有推送
     * @param date
     */
    Long countTotalPush(@Param("date")String date);

}
