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
     *
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
     *
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

    /**
     * 统计评论数
     *
     * @param date
     * @param category
     * @return
     */
    long countCommentByDate(@Param("date") String date, @Param("category") String category);

    /**
     * 统计评论人数
     *
     * @param date
     * @param category
     * @return
     */
    long countCommentUserByDate(@Param("date") String date, @Param("category") String category);

    /**
     * 查看是否点赞
     *
     * @param commentId
     * @param userId
     * @return
     */
    Integer hasLike(@Param("commentId") String commentId, @Param("userId") String userId);

    /**
     * 更新评论点赞状态
     *
     * @param commentId
     * @param userId
     * @param status
     * @return
     */
    void updateCommentLikeStatus(@Param("commentId") String commentId, @Param("userId") String userId, @Param("status") String status);

    /**
     * 添加评论点赞状态
     *
     * @param commentId
     * @param userId
     * @return
     */
    void addCommentLike(@Param("commentId") String commentId, @Param("userId") String userId);
}
