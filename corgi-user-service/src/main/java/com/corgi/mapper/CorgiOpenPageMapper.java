package com.corgi.mapper;

import com.corgi.user.entity.CorgiOpenPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiOpenPageMapper {

    /**
     * 添加banner
     *
     * @param corgiOpenPage
     */
    void addOpenPage(@Param("page") CorgiOpenPage corgiOpenPage);

    /**
     * 修改banner
     *
     * @param corgiOpenPage
     */
    void updateOpenPage(@Param("page") CorgiOpenPage corgiOpenPage);

    /**
     * 删除banner
     *
     * @param pageId
     */
    void deleteOpenPage(@Param("pageId") Integer pageId);

    /**
     * 搜索banner
     *
     * @param corgiOpenPage
     * @return
     */
    List<CorgiOpenPage> listOpenPage(@Param("page") CorgiOpenPage corgiOpenPage);

    /**
     * 查询生日开屏
     *
     * @param userId
     * @return
     */
    List<CorgiOpenPage> getBirthdayOpenPage(@Param("userId") String userId, @Param("date") String date, @Param("time") Long time);

}
