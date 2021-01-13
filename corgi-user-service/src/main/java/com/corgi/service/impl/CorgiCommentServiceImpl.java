package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCommentMapper;
import com.corgi.user.api.CorgiCommentService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.api.CorgiVlogService;
import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.ActivityMessage;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.rmi.activation.ActivationID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    @Reference
    private CorgiVlogService corgiVlogService;

    @Override
    public ActivityComment addActivityComment(ActivityComment activityComment) {
        String userId = activityComment.getCommentUserId();
        if ("-1".equals(userId) || StringUtils.isEmpty(userId)) {
            userId = "1";
        }
        UserDetail commentUserDetail = corgiUserService.getUserDetail(userId, null);
        activityComment.setCommentUserName(commentUserDetail.getNickname());
        activityComment.setCommentUserAvatar(commentUserDetail.getUserPics().get(0).getPicUrl());

        if (!StringUtils.isEmpty(activityComment.getReplyUserId())) {
            UserDetail replyUserDetail = corgiUserService.getUserDetail(activityComment.getReplyUserId(), null);
            activityComment.setReplyUserName(replyUserDetail.getNickname());
            activityComment.setReplyUserAvatar(replyUserDetail.getUserPics().get(0).getPicUrl());
        }
        CorgiVlog corgiVlog = CorgiVlog.builder().activityId(activityComment.getActivityId()).commentCount(1).build();
        corgiVlogService.addVlogCount(corgiVlog);
        activityComment.setCommentId(UUID.randomUUID().toString());
        corgiCommentMapper.addActivityComment(activityComment);
        if (!commentUserDetail.getUserId().equals(activityComment.getUserId())) {
            corgiToolService.addActivityMessage(ActivityMessage.builder()
                    .activityId(activityComment.getActivityId())
                    .fromUserAvatar(commentUserDetail.getUserPics().get(0).getPicUrl())
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
                    .fromUserAvatar(commentUserDetail.getUserPics().get(0).getPicUrl())
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
            CorgiVlog corgiVlog = CorgiVlog.builder().activityId(activityComment.getActivityId()).commentCount(-1).build();
            corgiVlogService.addVlogCount(corgiVlog);
            corgiToolService.deleteActivityMessageByMessage(ActivityMessage.builder()
                    .fromUserId(activityComment.getCommentUserId())
                    .commentId(commentId)
                    .build());
        }
    }

    @Override
    public List<ActivityComment> getActivityComment(String activityId) {
        List<ActivityComment> comments = corgiCommentMapper.getActivityComment(activityId);
        return buildComments(comments);
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

    private List<ActivityComment> buildComments(List<ActivityComment> activityComments) {
        List<ActivityComment> results = new ArrayList<>();
        HashMap<String, ActivityComment> commentHashMap = new HashMap<>();
        if (activityComments != null) {
            for (ActivityComment comment : activityComments) {
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
        for (ActivityComment comment : comments) {
            if (commentId.equals(comment.getCommentId())) {
                commentHashMap.put(commentId, comment);
                return comment;
            }
        }
        return null;
    }
}
