package com.corgi.mapper;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CheckPic;
import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.UserPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCommentMapper {
    /**
     * 添加活动评论
     *
     * @param activityComment
     */
    void addActivityComment(@Param("comment") ActivityComment activityComment);

    /**
     * 删除活动评论
     * @param commentId
     */
    void deleteActivityComment(@Param("commentId") String commentId);

    /**
     * 获取活动评论
     *
     * @param activityId
     * @return
     */
    List<ActivityComment> getActivityComment(@Param("activityId") String activityId);

    /**
     * 根据ID获取comment
     * @param commentId
     * @return
     */
    ActivityComment getActivityCommentByCommentId(@Param("commentId") String commentId);


    /**
     * 获取评论数
     *
     * @param activityId
     * @return
     */
    Long countActivityComment(@Param("activityId") String activityId);

    /**
     * 获取最后一条评论
     *
     * @param activityId
     * @param userId
     * @return
     */
    ActivityComment getLastActivityComment(@Param("activityId") String activityId, @Param("userId") String userId);

}
