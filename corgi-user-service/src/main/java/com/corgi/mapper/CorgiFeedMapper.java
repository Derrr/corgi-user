package com.corgi.mapper;

import com.corgi.user.entity.CorgiFeed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFeedMapper {

    /**
     * 获取feed
     *
     * @param userId
     * @return
     */
    List<String> getUnviewFeed(@Param("userId") String userId, @Param("index") String index);

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

}
