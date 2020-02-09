package com.corgi.mapper;

import com.corgi.user.entity.UserMatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserMatchMapper {
    /**
     * 获取族类匹配
     *
     * @param group1
     * @param group2
     * @return
     */
    Integer getGroupMatch(@Param("group1") String group1, @Param("group2") String group2);

    /**
     * 获取性格匹配
     *
     * @param ctr1
     * @param ctr2
     * @return
     */
    Integer getCharacterMatch(@Param("ctr1") String ctr1, @Param("ctr2") String ctr2);

    /**
     * 添加匹配缓存
     *
     * @param userId1
     * @param userId2
     * @param match
     */
    void addMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2, @Param("match") Double match);

    /**
     * 更新匹配缓存
     *
     * @param userId1
     * @param userId2
     * @param match
     */
    void updateMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2, @Param("match") Double match);

    /**
     * 获取匹配缓存
     *
     * @param userId1
     * @param userId2
     */
    Double getMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2);

    /**
     * 批量获取匹配
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<UserMatch> getMatchByPage(@Param("userId") String userId, @Param("start") int start, @Param("size") int size);
}
