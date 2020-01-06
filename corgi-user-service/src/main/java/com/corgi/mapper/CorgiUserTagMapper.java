package com.corgi.mapper;

import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserTagMapper {
    /**
     * 获取所有标签
     *
     * @return
     */
    List<String> getTags();

    /**
     * 获取用户标签
     *
     * @param userId
     * @return
     */
    List<String> getUserTag(@Param("userId") String userId);

    /**
     * 删除用户标签
     *
     * @param userId
     */
    void deleteUserTag(@Param("userId") String userId);

    /**
     * 添加用户标签
     *
     * @param userId
     * @param tag
     */
    void addUserTag(@Param("userId") String userId, @Param("tag") String tag);

    /**
     * 获取兴趣
     *
     * @param category
     * @return
     */
    List<String> getCategoryInterests(@Param("category") String category);

    /**
     * 获取用户兴趣
     *
     * @param userId
     * @return
     */
    List<UserInterest> getUserInterests(@Param("userId") String userId);

    /**
     * 删除用户兴趣
     *
     * @param userId
     * @param category
     */
    void deleteUserInterests(@Param("userId") String userId, @Param("category") String category);

    /**
     * 添加用户兴趣
     *
     * @param userId
     * @param category
     * @param interest
     */
    void addUserInterests(@Param("userId") String userId, @Param("category") String category, @Param("interest") String interest);

}
