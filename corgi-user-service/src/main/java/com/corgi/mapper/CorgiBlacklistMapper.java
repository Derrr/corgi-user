package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBlacklistMapper {

    /**
     * 移除黑名单
     * @param userId
     * @param blackId
     */
    void deleteBlacklist(@Param("userId") String userId, @Param("blackId")String blackId);

    /**
     * 添加黑名单
     * @param userId
     * @param blackId
     */
    void addBlacklist(@Param("userId") String userId, @Param("blackId")String blackId);

    /**
     * 获取黑名单
     * @param userId
     * @return
     */
    List<String> getBlacklist(@Param("userId")String userId);

    /**
     * 获取被拉黑名单
     * @param userId
     * @return
     */
    List<String> getBeBlacklist(@Param("userId")String userId);

    /**
     * 删除所有相关黑名单
     * @param userId
     */
    void deleteAll(@Param("userId")String userId);



}
