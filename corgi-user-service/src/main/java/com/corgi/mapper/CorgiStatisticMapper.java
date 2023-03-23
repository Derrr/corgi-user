package com.corgi.mapper;

import com.corgi.entity.CorgiStatistic;
import com.corgi.entity.CorgiTopic;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiStatisticMapper {

    /**
     * 添加统计
     *
     * @param table
     * @param date
     * @param count
     */
    void addCount(@Param("table") String table, @Param("date") String date, @Param("count") Long count);

    /**
     * 删除统计
     *
     * @param table
     * @param date
     */
    void deleteCount(@Param("table") String table, @Param("date") String date);


    /**
     * 获取统计
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    List<CorgiStatistic> getCount(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 总计统计
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    Long sumCount(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 创建活跃时间
     *
     * @param date
     */
    void initMap(@Param("date") String date, @Param("table") String table);

    /**
     * 更新数据
     *
     * @param table
     * @param date
     * @param key
     * @param count
     */
    void updateMap(@Param("table") String table, @Param("date") String date, @Param("key") String key, @Param("count") Long count);

    /**
     * 获取时间段列表
     *
     * @param beginDate
     * @param endDate
     * @param table
     * @return
     */
    List<HashMap> getMap(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取所有时间段List
     *
     * @param table
     * @param beginDate
     * @param endDate
     * @return
     */
    List<CorgiStatistic> getList(@Param("table") String table, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 添加List
     *
     * @param table
     * @param date
     * @param name
     * @param count
     */
    void addList(@Param("table") String table, @Param("date") String date, @Param("name") String name, @Param("count") Long count);

    /**
     * 删除List
     *
     * @param table
     * @param name
     * @param date
     */
    void deleteList(@Param("table") String table, @Param("date") String date, @Param("name") String name);

    /**
     * 更新List
     *
     * @param table
     * @param date
     * @param name
     * @param count
     */
    void updateList(@Param("table") String table, @Param("date") String date, @Param("name") String name, @Param("count") Long count);

    /**
     * 天加用户留存数
     *
     * @param date
     * @param registerDate
     * @param stayCount
     * @param count
     */
    void addUserStay(@Param("date") String date, @Param("registerDate") String registerDate, @Param("stayCount") String stayCount, @Param("count") Long count);

    /**
     * 获取用户留存
     *
     * @param beginDate
     * @param endDate
     * @param stayCount
     * @return
     */
    List<HashMap> getUserStay(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("stayCount") String stayCount);

    /**
     * 获取用户路径
     *
     * @param beginDate
     * @param endDate
     * @param userId
     * @return
     */
    List<UserTrace> getUserTrace(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("userId") String userId);

    /**
     * 获取最后的trace
     *
     * @param userId
     * @return
     */
    List<UserTrace> getLastUserTrace(@Param("userId") String userId);

    /**
     * 添加用户路径
     *
     * @param userTrace
     */
    void addUserTrace(@Param("userTrace") UserTrace userTrace);

    /**
     * 更新类型
     *
     * @param userTrace
     */
    void updateTraceStatus(@Param("userTrace") UserTrace userTrace);

    /**
     * 更新用户停留
     *
     * @param userTrace
     */
    void updateTraceStay(@Param("userTrace") UserTrace userTrace);

    /**
     * 统计用户轨迹
     *
     * @param date
     * @return
     */
    List<HashMap> countUserTrace(@Param("date") String date);

    /**
     * 统计用户耗时
     *
     * @param date
     * @return
     */
    Double countTotalUserTrace(@Param("date") String date);

    /**
     * 添加用户轨迹统计
     *
     * @param type
     * @param date
     * @param time
     */
    void addUserTraceSum(@Param("type") String type, @Param("date") String date, @Param("time") Double time);

    /**
     * 获取用户轨迹统计
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HashMap> getUserTraceSum(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 添加性格
     *
     * @param openId
     * @param ctr
     */
    void addCharacter(@Param("openId") String openId, @Param("ctr") String ctr);

    /**
     * 获取性格
     *
     * @param openId
     * @return
     */
    String getCharacter(@Param("openId") String openId);

    /**
     * 修改性格
     *
     * @param openId
     * @param ctr
     */
    void updateCharacter(@Param("openId") String openId, @Param("ctr") String ctr);

    /**
     * 查询付费行为
     *
     * @param behaviorReq
     * @return
     */
    CorgiBehaviorStatistics getPayTypeBehavior(@Param("behavior") CorgiBehaviorReq behaviorReq);

    /**
     * 查询付费行为
     *
     * @param behaviorReq
     * @return
     */
    CorgiBehaviorStatistics getActivityTypeBehavior(@Param("behavior") CorgiBehaviorReq behaviorReq, @Param("userName") String userName);

    /**
     * 获取最多评论
     * @param contentReq
     * @return
     */
    CorgiContentStatistics getMostComment(@Param("contentReq") CorgiContentReq contentReq);

    /**
     * 最多涨粉
     * @param contentReq
     * @return
     */
    CorgiContentStatistics getMostFollow(@Param("contentReq") CorgiContentReq contentReq);

    /**
     * 最多点赞
     * @param contentReq
     * @return
     */
    CorgiContentStatistics getMostLike(@Param("contentReq") CorgiContentReq contentReq);

    /**
     * 最多分享
     * @param contentReq
     * @return
     */
    CorgiContentStatistics getMostShare(@Param("contentReq") CorgiContentReq contentReq);

    /**
     * 最多获赞用户
     * @param contentReq
     * @return
     */
    CorgiContentStatistics getMostUserLike(@Param("contentReq") CorgiContentReq contentReq);
}
