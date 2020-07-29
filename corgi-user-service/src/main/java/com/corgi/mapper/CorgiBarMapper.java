package com.corgi.mapper;

import com.corgi.user.entity.BarProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBarMapper {

    /**
     * 添加商户
     *
     * @param barProfile
     */
    void addBar(@Param("bar") BarProfile barProfile);

    /**
     * 修改商户
     *
     * @param barProfile
     */
    void updateBar(@Param("bar") BarProfile barProfile);

    /**
     * 获取当前最大商户ID
     *
     * @return
     */
    String getMaxBarId();

    /**
     * 获取所有商户列表
     *
     * @return
     */
    List<BarProfile> getBarList(@Param("status")String status);

    /**
     * 根据城市获取所有商户列表
     *
     * @return
     */
    List<BarProfile> getBarListByCity(@Param("city")String city);

    /**
     * 获取商户信息
     *
     * @param barId
     * @return
     */
    BarProfile getBar(@Param("barId") String barId);

    /**
     * 统计在周围且关注了企业用户的人数
     * @param barId
     * @param userIds
     * @return
     */
    Long countBarFollow(@Param("barId")String barId, @Param("userIds")String userIds);

    /**
     * 搜索商户
     *
     * @param barProfile
     * @return
     */
    List<BarProfile> searchBar(@Param("bar")BarProfile barProfile);

}
