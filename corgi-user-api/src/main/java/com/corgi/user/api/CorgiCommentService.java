package com.corgi.user.api;


import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.ActivityLike;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCommentService {
    ActivityComment addActivityComment(ActivityComment activityComment);

    void deleteActivityComment(String commentId);

    List<ActivityComment> getActivityComment(String activityId, Integer commentId, Integer size, String userId);

    List<ActivityComment> getHotComment(String activityId, String userId);

    List<ActivityComment> queryComment(ActivityComment query, Integer size);

    List<ActivityComment> listComment(ActivityComment query, Integer size);

    Long countActivityComment(String activityId);

    Integer countActivityCommentByStatus(String activityId, String status);

    ActivityComment getLastComment(String activityId, String userId);

    long countCommentByDate(String date, String category);

    long countCommentUserByDate(String date, String category);

    ActivityComment likeComment(String commentId, String userId);

    void disLikeComment(String commentId, String userId);
}
