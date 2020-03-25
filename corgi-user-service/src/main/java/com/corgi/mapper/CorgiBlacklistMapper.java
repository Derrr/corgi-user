package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.CorgiReport;
import com.corgi.user.entity.UserBasic;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

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
    void deleteBlacklist(@Param("userId") String userId, @Param("blackId") String blackId);

    /**
     * 添加黑名单
     *
     * @param userId
     * @param blackId
     */
    void addBlacklist(@Param("userId") String userId, @Param("blackId") String blackId);

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
    void updateReportStatus(@Param("reportId") String reportId, @Param("status") String status);

    /**
     * 获取举报列表
     *
     * @param status
     * @return
     */
    List<CorgiReport> getReport(@Param("status") String status);


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

}
