package com.corgi.mapper;

import com.corgi.user.entity.HotActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiHotActivityMapper {

    /**
     * 添加热门活动
     *
     * @param hotActivity
     */
    void addHotActivity(@Param("hot") HotActivity hotActivity);

    /**
     * 修改热门活动
     *
     * @param hotActivity
     */
    void updateHotActivity(@Param("hot") HotActivity hotActivity);

    /**
     * 获取所有热门活动列表
     *
     * @return
     */
    List<HotActivity> getHotActivityList();

    /**
     * 根据城市获取所有热门活动列表
     *
     * @return
     */
    List<HotActivity> getHotActivityListByCity(@Param("city") String city, @Param("time") String time);

    /**
     * 删除热门活动
     *
     * @param hotId
     * @return
     */
    void deleteHotActivity(@Param("hotId") String hotId);

    /**
     * 搜索热门活动
     *
     * @param hotActivity
     * @return
     */
    List<HotActivity> searchHotActivity(@Param("hot") HotActivity hotActivity);

}
