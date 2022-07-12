package com.corgi.mapper;

import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.*;
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
     * 获取被点赞数最多的动态
     *
     * @param activityQuery
     * @param limit
     * @return
     */
    List<CorgiActivity> getPopularActivity(@Param("query") ActivityQuery activityQuery, @Param("size") Integer limit);


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
     *
     * @param date
     * @return
     */
    List<UserProfile> getPastBillboard(@Param("date") String date);

    /**
     * 获取过去经常上榜用户
     *
     * @param date
     * @return
     */
    List<UserProfile> getPastPopularBillboard(@Param("date") String date);

    /**
     * 根据昵称更新榜单
     *
     * @param fromId
     * @param toId
     * @param date
     */
    void updateBillboard(@Param("fromId") String fromId, @Param("toId") String toId, @Param("date") String date);

    /**
     * 根据ID更新榜单顺序
     *
     * @param userId
     * @param order
     * @param date
     */
    void updateBillboardOrder(@Param("userId") String userId, @Param("date") String date, @Param("order") Integer order);

    /**
     * 根据昵称获取ID
     *
     * @param nickname
     * @return
     */
    String getUserIdByNickname(@Param("nickname") String nickname);


    /**
     * 获取榜单数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<Billboard> getBillboardByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 添加榜单活动
     *
     * @param activity
     */
    void addActivityBillboard(@Param("activity") ActivityBillboard activity);

    void addActivityBillboardStatus(@Param("activity") ActivityBillboard activity);

    void updateActivityBillboardTime(@Param("activityId") String activityId);

    /**
     * 删除榜单活动
     *
     * @param activity
     */
    void deleteActivityBillboard(@Param("activity") ActivityBillboard activity);


    /**
     * 获取榜单活动
     *
     * @return
     */
    List<String> getActivityBillboard(@Param("date") String date);

    /**
     * 获取榜单活动
     *
     * @return
     */
    List<ActivityBillboard> getAllActivityBillboard(@Param("activity") ActivityBillboard activity);

    /**
     * 获取榜单活动
     *
     * @return
     */
    String getOnboardDate(@Param("userId") String userId, @Param("date") String date);

    /**
     * 统计上榜次数
     *
     * @return
     */
    Integer countOnBoard(@Param("userId") String userId);

    /**
     * 获取上榜时间
     *
     * @return
     */
    List<String> getBillboardTimeById(@Param("userId") String userId);

    /**
     * 添加付费上榜动态
     *
     * @return
     */
    void addPaidBillboard(@Param("paid") PaidBillboard paidBillboard);

    /**
     * 修改付费上榜动态
     *
     * @return
     */
    void updatePaidBillboard(@Param("paid") PaidBillboard paidBillboard);

    /**
     * 统计付费
     */
    Integer countPaidBillboard(@Param("paid") PaidBillboard paidBillboard);

    /**
     * 查询付费榜单
     */
    List<PaidBillboard> getPaidBillboard(@Param("paid") PaidBillboard paidBillboard, @Param("start") Integer start, @Param("size") Integer size);
}
