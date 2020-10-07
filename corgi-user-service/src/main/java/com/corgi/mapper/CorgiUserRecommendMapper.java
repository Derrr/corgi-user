package com.corgi.mapper;

import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserRecommendMapper {
    /**
     * 初始化用户
     * @param userId
     * @param recId
     */
    void insertUserRecommend(@Param("userId") String userId, @Param("recId") String recId);

    /**
     * 添加推荐权重
     * @param userId
     * @param recId
     */
    void addRecommend(@Param("userId") String userId, @Param("recId") String recId);

    /**
     * 更新推荐状态
     * @param userId
     * @param recId
     * @param status
     */
    void updateStatus(@Param("userId") String userId, @Param("recId") String recId, @Param("status") String status, @Param("opt")String opt);

    /**
     * 获取推荐用户
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<UserProfile> getRecUsers(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 清空推荐
     * @param userId
     */
    void clearRecUsers(@Param("userId")String userId);

}
