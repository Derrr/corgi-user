package com.corgi.mapper;

import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFavorActivityMapper {
    /**
     * 收藏
     *
     * @param userId
     * @param activityId
     */
    void addFavor(@Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * 取消收藏
     *
     * @param userId
     * @param activityId
     */
    void deleteFavor(@Param("userId") String userId, @Param("activityId") String activityId);


    /**
     * 分页获取收藏
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<String> getActivity(@Param("userId") String userId, @Param("start") int start, @Param("size") int size);

    /**
     * 获取是否收藏了某店铺
     * @param userId
     * @param activityId
     * @return
     */
    int countActivity(@Param("userId") String userId, @Param("activityId") String activityId);
}
