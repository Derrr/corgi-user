package com.corgi.mapper;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.support.UserPositionSupporter;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiPicMapper {
    /**
     * 添加用户图片
     *
     * @param userPic
     */
    void addUserPic(@Param("userPic") UserPic userPic);

    /**
     * 删除用户图片
     *
     * @param picId
     */
    void deleteUserPic(@Param("picId") String picId);

    /**
     * 获取用户图片
     *
     * @param userId
     * @return
     */
    List<UserPic> getUserPic(@Param("userId") String userId);

    /**
     * 添加活动图片
     *
     * @param activityPic
     */
    void addActivityPic(@Param("activityPic") ActivityPic activityPic);

    /**
     * 删除活动图片
     *
     * @param picId
     */
    void deleteActivityPic(@Param("picId") String picId);

    /**
     * 获取活动图片
     *
     * @param activityId
     * @return
     */
    List<ActivityPic> getActivityPic(@Param("activityId") String activityId);
}
