package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityComment implements Serializable {
    private Integer id;
    private String commentId;

    public String getParentCommentId() {
        if (parentCommentId == null || "".equals(parentCommentId)) {
            return "0";
        }
        return parentCommentId;
    }

    private String parentCommentId;
    private String activityId;
    private String userId;
    private String content;
    private String ctime;

    private String commentUserId;
    private String commentUserName;
    private String commentUserAvatar;

    private String replyUserId;
    private String replyUserName;
    private String replyUserAvatar;

    private Integer likeCount;

    private Integer hasLike = 0;

    private boolean hasMore;
    private boolean hasAuthor;

    List<ActivityComment> childComments;

    public void addChildComment(ActivityComment comment) {
        if (this.childComments == null) {
            this.childComments = new ArrayList<ActivityComment>();
        }
        childComments.add(0, comment);
    }
}
