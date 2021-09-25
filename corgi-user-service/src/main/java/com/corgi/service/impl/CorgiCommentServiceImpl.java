package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCommentMapper;
import com.corgi.user.api.CorgiCommentService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.ActivityMessage;
import com.corgi.user.entity.UserDetail;
import com.corgi.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = CorgiCommentService.class)
@Slf4j
@Component
public class CorgiCommentServiceImpl implements CorgiCommentService {
    @Autowired
    private CorgiCommentMapper corgiCommentMapper;
    @Autowired
    private CorgiUserService corgiUserService;
    @Autowired
    private CorgiToolService corgiToolService;

    @Override
    public ActivityComment addActivityComment(ActivityComment activityComment) {
        String userId = activityComment.getCommentUserId();
        if ("-1".equals(userId) || StringUtils.isEmpty(userId)) {
            userId = "1";
        }
        UserDetail commentUserDetail = corgiUserService.getUserDetail(userId, null);
        activityComment.setCommentUserName(commentUserDetail.getNickname());
        activityComment.setCommentUserAvatar(commentUserDetail.getAvatar());

        if (!StringUtils.isEmpty(activityComment.getReplyUserId())) {
            UserDetail replyUserDetail = corgiUserService.getUserDetail(activityComment.getReplyUserId(), null);
            activityComment.setReplyUserName(replyUserDetail.getNickname());
            activityComment.setReplyUserAvatar(replyUserDetail.getAvatar());
        }
        activityComment.setCommentId(UUID.randomUUID().toString());
        corgiCommentMapper.addActivityComment(activityComment);
        if (!commentUserDetail.getUserId().equals(activityComment.getUserId())) {
            corgiToolService.addActivityMessage(ActivityMessage.builder()
                    .activityId(activityComment.getActivityId())
                    .fromUserAvatar(commentUserDetail.getAvatar())
                    .fromUserId(commentUserDetail.getUserId())
                    .fromUserName(commentUserDetail.getNickname())
                    .toUserId(activityComment.getUserId())
                    .time(System.currentTimeMillis())
                    .content(activityComment.getContent())
                    .commentId(activityComment.getCommentId())
                    .messageType(ActivityMessage.COMMENT)
                    .build());
        }
        if (!StringUtils.isEmpty(activityComment.getReplyUserId()) && !activityComment.getReplyUserId().equals(commentUserDetail.getUserId())) {
            corgiToolService.addActivityMessage(ActivityMessage.builder()
                    .activityId(activityComment.getActivityId())
                    .fromUserAvatar(commentUserDetail.getAvatar())
                    .fromUserId(commentUserDetail.getUserId())
                    .fromUserName(commentUserDetail.getNickname())
                    .toUserId(activityComment.getReplyUserId())
                    .time(System.currentTimeMillis())
                    .commentId(activityComment.getCommentId())
                    .content(activityComment.getContent())
                    .messageType(ActivityMessage.COMMENT)
                    .build());
        }
        return activityComment;
    }

    @Override
    public void deleteActivityComment(String commentId) {
        ActivityComment activityComment = corgiCommentMapper.getActivityCommentByCommentId(commentId);
        if (activityComment != null) {
            corgiCommentMapper.deleteActivityComment(commentId);
            corgiToolService.deleteActivityMessageByMessage(ActivityMessage.builder()
                    .fromUserId(activityComment.getCommentUserId())
                    .commentId(commentId)
                    .build());
        }
    }

    @Override
    public List<ActivityComment> getActivityComment(String activityId, Integer commentId, Integer size, String userId) {
        if (size != null && size > 0) {
            List<ActivityComment> comments = corgiCommentMapper.getParentComment(activityId, commentId, size);
            return buildParentComments(comments, userId);
        } else {
            List<ActivityComment> comments = corgiCommentMapper.getActivityComment(activityId);
            return buildComments(comments, userId);
        }
    }

    @Override
    public List<ActivityComment> getHotComment(String activityId, String userId) {
        List<ActivityComment> comments = corgiCommentMapper.getHotComment(activityId);
        if (CollectionUtils.isEmpty(comments) || comments.size() < 5) {
            return new ArrayList<>();
        }
        Long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ActivityComment> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ActivityComment comment = comments.get(i);
            comment.setTimeShow(TimeUtil.buildTimeText(comment.getCtime(), now, sdf));
            comment.setHasLike(corgiCommentMapper.hasLike(comment.getCommentId(), userId));
            result.add(comment);
        }
        return result;
    }

    @Override
    public List<ActivityComment> queryComment(ActivityComment query, Integer size) {
        return corgiCommentMapper.queryComment(query, size);
    }

    @Override
    public List<ActivityComment> listComment(ActivityComment query, Integer size) {
        return corgiCommentMapper.listComment(query);
    }

    @Override
    public Long countActivityComment(String activityId) {
        return corgiCommentMapper.countActivityComment(activityId);
    }

    @Override
    public ActivityComment getLastComment(String activityId, String userId) {
        return corgiCommentMapper.getLastActivityComment(activityId, userId);
    }

    @Override
    public long countCommentByDate(String date, String category) {
        return corgiCommentMapper.countCommentByDate(date, category);
    }

    @Override
    public long countCommentUserByDate(String date, String category) {
        return corgiCommentMapper.countCommentUserByDate(date, category);
    }

    @Override
    public ActivityComment likeComment(String commentId, String userId) {
        corgiCommentMapper.addCommentLike(commentId, userId);
        corgiCommentMapper.updateCommentLikeStatus(commentId, userId, "1");
        ActivityComment activityComment = corgiCommentMapper.getActivityCommentByCommentId(commentId);
        if (!activityComment.getCommentUserId().equals(userId)) {
            UserDetail likeUserDetail = corgiUserService.getUserDetailBasic(userId);
            corgiToolService.addActivityMessage(ActivityMessage.builder()
                    .activityId(activityComment.getActivityId())
                    .fromUserAvatar(likeUserDetail.getAvatar())
                    .fromUserId(likeUserDetail.getUserId())
                    .fromUserName(likeUserDetail.getNickname())
                    .toUserId(activityComment.getCommentUserId())
                    .time(System.currentTimeMillis())
                    .content("点赞了你的评论")
                    .commentId(activityComment.getCommentId())
                    .messageType(ActivityMessage.LIKE)
                    .build());
        }
        return activityComment;
    }

    @Override
    public void disLikeComment(String commentId, String userId) {
        corgiCommentMapper.updateCommentLikeStatus(commentId, userId, "0");
        ActivityComment activityComment = corgiCommentMapper.getActivityCommentByCommentId(commentId);
        corgiToolService.deleteActivityMessageByMessage(ActivityMessage.builder()
                .fromUserId(userId)
                .activityId(activityComment.getActivityId())
                .commentId(commentId)
                .messageType(ActivityMessage.LIKE)
                .build());
    }

    private List<ActivityComment> buildParentComments(List<ActivityComment> activityComments, String userId) {
        if (!CollectionUtils.isEmpty(activityComments)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long now = System.currentTimeMillis();
            for (ActivityComment comment : activityComments) {
                comment.setTimeShow(TimeUtil.buildTimeText(comment.getCtime(), now, sdf));
                if (comment.getLikeCount() != null && comment.getLikeCount() > 0) {
                    comment.setHasLike(corgiCommentMapper.hasLike(comment.getCommentId(), userId));
                }
                List<ActivityComment> comments = corgiCommentMapper.getChildrenComment(comment.getCommentId());
                if (!CollectionUtils.isEmpty(comments)) {
                    for (ActivityComment comment1 : comments) {
                        if (userId.equals(comment1.getCommentUserId())) {
                            comment.setHasAuthor(true);
                            break;
                        }
                    }
                    comment.setTimeShow(TimeUtil.buildTimeText(comment.getCtime(), now, sdf));
                    comment.setChildComments(comments);
                    comment.setChildCount(comments.size());
                }
            }
        }
        return activityComments;
    }

    private List<ActivityComment> buildComments(List<ActivityComment> activityComments, String userId) {
        List<ActivityComment> results = new ArrayList<>();
        HashMap<String, ActivityComment> commentHashMap = new HashMap<>();
        if (activityComments != null) {
            Long now = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ActivityComment comment : activityComments) {
                comment.setTimeShow(TimeUtil.buildTimeText(comment.getCtime(), now, sdf));
                if (comment.getLikeCount() != null && comment.getLikeCount() > 0) {
                    comment.setHasLike(corgiCommentMapper.hasLike(comment.getCommentId(), userId));
                }
                if ("0".equals(comment.getParentCommentId())) {
                    results.add(comment);
                    commentHashMap.put(comment.getCommentId(), comment);
                } else {
                    ActivityComment parentComment = findComment(comment.getParentCommentId(), activityComments, commentHashMap);
                    if (parentComment != null) {
                        parentComment.addChildComment(comment);
                    }
                }
            }
        }
        return results;
    }

    private ActivityComment findComment(String commentId, List<ActivityComment> comments, HashMap<String, ActivityComment> commentHashMap) {
        ActivityComment result = commentHashMap.get(commentId);
        if (result != null) {
            return result;
        }
        Long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ActivityComment comment : comments) {
            comment.setTimeShow(TimeUtil.buildTimeText(comment.getCtime(), now, sdf));
            if (commentId.equals(comment.getCommentId())) {
                commentHashMap.put(commentId, comment);
                return comment;
            }
        }
        return null;
    }
}
