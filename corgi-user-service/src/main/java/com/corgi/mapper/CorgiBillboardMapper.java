package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiBillboardMapper {
    /**
     * 获取被点赞数最多的人
     *
     * @param userDetail
     * @param limit
     * @return
     */
    List<UserProfile> getPopularUsers(@Param("detail") UserDetail userDetail, @Param("date") String date, @Param("limit") Integer limit);

    /**
     * 获取点赞数最高的人
     *
     * @param userDetail
     * @param date
     * @param limit
     * @return
     */
    List<UserProfile> getPassionUsers(@Param("detail") UserDetail userDetail, @Param("date") String date, @Param("limit") Integer limit);

    /**
     * 获取互动量最高的人
     *
     * @param userDetail
     * @param date
     * @param limit
     * @return
     */
    List<UserProfile> getActiveUsers(@Param("detail") UserDetail userDetail, @Param("date") String date, @Param("limit") Integer limit);

    /**
     * 添加榜单人员
     *
     * @param userId
     * @param count
     * @param countType
     * @param date
     */
    void addBillboardUser(@Param("userId") String userId, @Param("count") Integer count, @Param("countType") String countType, @Param("date") String date);

    /**
     * 获取榜单人员
     *
     * @param date
     * @return
     */
    List<UserProfile> getBillboardUsers(@Param("date") String date);

    /**
     * 清除榜单人员
     *
     * @param date
     */
    void cleanBillboardByDate(@Param("date") String date);

    /**
     * 获取过去上榜用户
     * @param date
     * @return
     */
    List<UserProfile> getPastBillboard(@Param("date") String date);
}
