package com.corgi.mapper;

import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserFollowMapper {
    /**
     * 添加关注
     *
     * @param userId
     * @param followId
     */
    void addFollowUser(@Param("userId") String userId, @Param("followId") String followId);

    /**
     * 取消关注
     *
     * @param userId
     * @param followId
     */
    void deleteFollowUser(@Param("userId") String userId, @Param("followId") String followId);

    /**
     * 判断是否关注
     *
     * @param userId1
     * @param userId2
     * @return
     */
    int countFollow(@Param("userId1") String userId1, @Param("userId2") String userId2);

    /**
     * 获取关注人
     *
     * @param userId
     * @return
     */
    List<String> getFollowUser(@Param("userId") String userId);


    /**
     * 对关注人排序
     *
     * @param userId
     * @param type
     * @param lat
     * @param lng
     * @param start
     * @param size
     * @return
     */
    List<UserProfile> getFollowUserByPage(@Param("userId") String userId, @Param("type") String type,
                                          @Param("lat") double lat, @Param("lng") double lng,
                                          @Param("start") Integer start, @Param("size") Integer size);
}
