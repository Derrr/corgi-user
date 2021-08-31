package com.corgi.mapper;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CheckPic;
import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

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
    void deleteUserPic(@Param("picId") String picId, @Param("userId") String userId);

    /**
     * 删除用户图片
     *
     * @param dataId
     */
    void deleteUserPicByDataId(@Param("dataId") String dataId);

    /**
     * 修改用户图片
     *
     * @param userPic
     */
    void updateUserPic(@Param("pic") UserPic userPic);

    /**
     * 修改用户图片状态
     *
     * @param dataId
     * @param status
     */
    void updateUserPicByDataId(@Param("dataId") String dataId, @Param("status") String status);

    /**
     * 根据DataId获取用户图片
     *
     * @param dataId
     * @return
     */
    UserPic getUserPicByDataId(@Param("dataId") String dataId);


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
     * 查找活动图片
     *
     * @param dataId
     */
    ActivityPic getActivityPicByDataId(@Param("dataId") String dataId);

    /**
     * 删除活动图片
     *
     * @param dataId
     */
    void deleteActivityPicByDataId(@Param("dataId") String dataId);

    /**
     * 更新活动图片状态
     *
     * @param dataId
     * @param status
     */
    void updateActivityPicByDataId(@Param("dataId") String dataId, @Param("status") String status);


    /**
     * 获取活动图片
     *
     * @param activityId
     * @return
     */
    List<ActivityPic> getActivityPic(@Param("activityId") String activityId);


    /**
     * 添加待审核图片
     *
     * @param checkPic
     */
    void addCheckPic(@Param("checkPic") CheckPic checkPic);

    /**
     * 更新审核图片状态
     *
     * @param dataId
     * @param status
     * @param userId
     */
    void updateCheckPic(@Param("dataId") String dataId, @Param("status") String status, @Param("userId") String userId);

    /**
     * 审核图片计数
     *
     * @param status
     * @return
     */
    long countCheckPic(@Param("status") String status, @Param("type") String type, @Param("userId") String userId);

    /**
     * 获取审核图片
     *
     * @param status
     * @param type
     * @param start
     * @param size
     * @return
     */
    List<CheckPic> getCheckPic(@Param("userId") String userId, @Param("status") String status, @Param("type") String type, @Param("start") long start, @Param("size") int size);

    /**
     * 获取审核图片
     *
     * @param type
     * @param sourceId
     * @return
     */
    List<CheckPic> getCheckPicBySourceId(@Param("type") String type, @Param("sourceId") String sourceId);

    /**
     * 获取审核图片
     *
     * @param dataId
     * @return
     */
    CheckPic getCheckPicByDataId(@Param("dataId") String dataId);

}
