package com.corgi.mapper;

import com.corgi.user.entity.ActivityView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiViewMapper {

    /**
     * 添加view
     *
     * @param activityView
     */
    void addView(@Param("view") ActivityView activityView);

    /**
     * 增加view count
     *
     * @return
     */
    void addViewCount(@Param("userId") String userId, @Param("activityId") String activityId);

}
