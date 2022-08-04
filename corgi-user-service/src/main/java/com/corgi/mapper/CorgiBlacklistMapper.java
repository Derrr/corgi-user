package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.CorgiReport;
import com.corgi.user.entity.UserBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBlacklistMapper {

    /**
     * 移除黑名单
     *
     * @param userId
     * @param blackId
     */
    Integer deleteBlacklist(@Param("userId") String userId, @Param("blackId") String blackId);

    /**
     * 添加黑名单
     *
     * @param userId
     * @param blackId
     */
    Integer addBlacklist(@Param("userId") String userId, @Param("blackId") String blackId);

    /**
     * 获取黑名单
     *
     * @param userId
     * @return
     */
    List<UserBasic> getBlacklist(@Param("userId") String userId);

    /**
     * 获取被拉黑名单
     *
     * @param userId
     * @return
     */
    List<String> getBeBlacklist(@Param("userId") String userId);

    /**
     * 删除所有相关黑名单
     *
     * @param userId
     */
    void deleteAll(@Param("userId") String userId);

    /**
     * 添加举报
     *
     * @param report
     */
    void addReport(@Param("report") CorgiReport report);

    /**
     * 修改举报状态
     *
     * @param reportId
     * @param status
     */
    void updateReportStatus(@Param("reportId") String reportId, @Param("status") String status, @Param("result")String result);

    /**
     * 获取举报列表
     *
     * @param corgiReport
     * @param start
     * @param size
     * @return
     */
    List<CorgiReport> getReport(@Param("report") CorgiReport corgiReport, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 计数举报
     * @param corgiReport
     * @return
     */
    Integer countReport(@Param("report") CorgiReport corgiReport);

    /**
     * 用户记述
     * @param corgiReport
     * @return
     */
    Integer countReportUser(@Param("report") CorgiReport corgiReport);

    /**
     * 添加举报图片
     *
     * @param reportId
     * @param url
     */
    void addReportPic(@Param("reportId") String reportId, @Param("url") String url);

    /**
     * 获取举报图片
     *
     * @param reportId
     * @return
     */
    List<String> getReportPic(@Param("reportId") String reportId);

    /**
     * 计数
     *
     * @param userId
     * @param blockId
     * @return
     */
    Integer countBlack(@Param("userId") String userId, @Param("blockId") String blockId);

}
