package com.corgi.mapper;

import com.corgi.user.entity.CorgiFeed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFeedMapper {

    Long countFeed(@Param("userId") String userId, @Param("index") String index);

    /**
     * 获取feed
     *
     * @param userId
     * @return
     */
    List<String> getUnviewFeed(@Param("userId") String userId, @Param("index") String index, @Param("size") Integer size);

    /**
     * 统计feed
     *
     * @param userId
     * @return
     */
    Integer countUnviewFeed(@Param("userId") String userId, @Param("index") String index);

    /**
     * 统计feed
     *
     * @param date
     * @return
     */
    Integer countViewFeed(@Param("date") String date, @Param("index") String index);


    /**
     * 添加feed
     *
     * @param corgiFeed
     * @param index
     */
    void addFeed(@Param("feed") CorgiFeed corgiFeed, @Param("index") String index);

    /**
     * 浏览feed
     *
     * @return
     */
    void viewFeed(@Param("userId") String userId, @Param("feed") String feed, @Param("index") String index);

    /**
     * 删除feed
     *
     * @param corgiFeed
     * @param index
     */
    void deleteFeed(@Param("feed") CorgiFeed corgiFeed, @Param("index") String index);

    /**
     * 删除feed
     *
     * @param corgiFeed
     * @param index
     */
    void clearFeed(@Param("feed") CorgiFeed corgiFeed, @Param("index") String index);
}
