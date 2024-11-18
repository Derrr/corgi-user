package com.corgi.mapper;

import com.corgi.user.entity.TlxActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface TlxActivityMapper {

    /**
     * 获取活动
     *
     * @param tlxActivity
     * @return
     */
    List<TlxActivity> getActivityList(@Param("activity") TlxActivity tlxActivity, @Param("start")Integer start, @Param("size")Integer size);

    /**
     * 获取活动
     *
     * @param id
     * @return
     */
    TlxActivity getActivity(@Param("id")String id);

    /**
     * 添加活动
     * @param tlxActivity
     */
    int addActivity(@Param("activity") TlxActivity tlxActivity);

    /**
     * 更新活动
     * @param tlxActivity
     */
    void updateActivity(@Param("activity") TlxActivity tlxActivity);

    /**
     * 更新状态
     * @param id
     */
    void updateStatus(@Param("id")String id, @Param("status")String status);

    /**
     * 刷新状态
     * @param version
     */
    void deleteActivityByVersion(@Param("version")String version);

    /**
     * 统计活动
     * @param tlxActivity
     */
    Integer countActivity(@Param("activity") TlxActivity tlxActivity);

}
