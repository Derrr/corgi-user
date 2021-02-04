package com.corgi.mapper;

import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserRecommendMapper {
    /**
     * 初始化用户
     *
     * @param userId
     * @param recId
     */
    void insertUserRecommend(@Param("userId") String userId, @Param("recId") String recId);

    /**
     * 添加推荐权重
     *
     * @param userId
     * @param recId
     */
    void addRecommend(@Param("userId") String userId, @Param("recId") String recId, @Param("weight") Integer weight);

    /**
     * 更新推荐状态
     *
     * @param userId
     * @param recId
     * @param status
     */
    void updateStatus(@Param("userId") String userId, @Param("recId") String recId, @Param("status") String status, @Param("opt") String opt);

    /**
     * 获取推荐用户
     *
     * @param userId
     * @param size
     * @return
     */
    List<UserProfile> getRecUsers(@Param("userId") String userId, @Param("size") Integer size);

    /**
     * 获取短视频推荐用户
     *
     * @param userId
     * @param size
     * @return
     */
    List<UserProfile> getVlogRecUsers(@Param("userId") String userId, @Param("size") Integer size);

    /**
     * 获取当地天菜创始人
     *
     * @param city
     * @return
     */
    List<UserProfile> getCityInfluencer(@Param("city") String city, @Param("userId") String userId, @Param("size") Integer size);

    /**
     * 获取全国天菜创始人
     *
     * @param city
     * @return
     */
    List<UserProfile> getNotCityInfluencer(@Param("city") String city, @Param("userId") String userId, @Param("size") Integer size);

    /**
     * 获取当地天红人
     *
     * @param city
     * @return
     */
    List<UserProfile> getCityPopulate(@Param("city") String city, @Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 清空推荐
     *
     * @param userId
     */
    void clearRecUsers(@Param("userId") String userId);

    /**
     * 根据权重删除
     *
     * @param userId
     * @param weight
     */
    void deleteByWeight(@Param("userId") String userId, @Param("weight") Integer weight);

    /**
     * 清理天菜创始人榜单
     */
    void clearInfluencerBillboard();

    /**
     * 初始化天菜创始人榜单
     */
    void initInfluencerBillboard();

    /**
     * 获取当地推荐动态
     *
     * @param city
     * @return
     */
    List<String> getCityImage(@Param("city") String city, @Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 获取全国推荐动态
     *
     * @param city
     * @return
     */
    List<String> getNotCityImage(@Param("city") String city, @Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    /**
     * 清空推荐
     *
     * @param userId
     */
    void clearRecActivity(@Param("userId")String userId);

    /**
     * 添加推荐权重
     *
     * @param userId
     * @param recId
     */
    void addRecommendActivity(@Param("userId") String userId, @Param("recId") String recId, @Param("weight") Integer weight);

}
