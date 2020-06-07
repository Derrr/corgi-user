package com.corgi.user.api;


import com.corgi.user.entity.ActivityComment;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiCommentService {
    void addActivityComment(ActivityComment activityComment);

    void deleteActivityComment(String commentId);

    List<ActivityComment> getActivityComment(String activityId);

    Long countActivityComment(String activityId);

    ActivityComment getLastComment(String activityId, String userId);
}
