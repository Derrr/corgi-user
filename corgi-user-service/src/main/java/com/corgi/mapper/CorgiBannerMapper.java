package com.corgi.mapper;

import com.corgi.user.entity.CorgiBanner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBannerMapper {

    /**
     * 添加banner
     *
     * @param corgiBanner
     */
    void addBanner(@Param("banner") CorgiBanner corgiBanner);

    /**
     * 修改banner
     *
     * @param corgiBanner
     */
    void updateBanner(@Param("banner") CorgiBanner corgiBanner);

    /**
     * 删除banner
     *
     * @param bannerId
     */
    void deleteBanner(@Param("bannerId") Integer bannerId);

    /**
     * 搜索banner
     *
     * @param corgiBanner
     * @return
     */
    List<CorgiBanner> listBanner(@Param("banner") CorgiBanner corgiBanner);

}
