package com.corgi.user.api;


import com.corgi.user.entity.ActivityComment;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCommentService {
    ActivityComment addActivityComment(ActivityComment activityComment);

    void deleteActivityComment(String commentId);

    List<ActivityComment> getActivityComment(String activityId);

    Long countActivityComment(String activityId);

    ActivityComment getLastComment(String activityId, String userId);

    long countCommentByDate(String date, String category);

    long countCommentUserByDate(String date, String category);
}
