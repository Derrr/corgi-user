package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiCommentMapper;
import com.corgi.user.api.CorgiCommentService;
import com.corgi.user.api.CorgiToolService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.ActivityComment;
import com.corgi.user.entity.ActivityMessage;
import com.corgi.user.entity.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

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
    public void addActivityComment(ActivityComment activityComment) {
        UserDetail commentUserDetail = corgiUserService.getUserDetail(activityComment.getCommentUserId(), null);
        activityComment.setCommentUserName(commentUserDetail.getNickname());
        activityComment.setCommentUserAvatar(commentUserDetail.getUserPics().get(0).getPicUrl());

        if (!StringUtils.isEmpty(activityComment.getReplyUserId())) {
            UserDetail replyUserDetail = corgiUserService.getUserDetail(activityComment.getReplyUserId(), null);
            activityComment.setCommentUserName(replyUserDetail.getNickname());
            activityComment.setCommentUserAvatar(replyUserDetail.getUserPics().get(0).getPicUrl());
        }

        corgiCommentMapper.addActivityComment(activityComment);
        corgiToolService.addActivityMessage(ActivityMessage.builder()
                .activityId(activityComment.getActivityId())
                .fromUserAvatar(commentUserDetail.getUserPics().get(0).getPicUrl())
                .fromUserId(commentUserDetail.getUserId())
                .fromUserName(commentUserDetail.getNickname())
                .toUserId(activityComment.getUserId())
                .time(System.currentTimeMillis())
                .messageType(ActivityMessage.COMMENT)
                .build());

    }

    @Override
    public List<ActivityComment> getActivityComment(String activityId) {
        return corgiCommentMapper.getActivityComment(activityId);
    }

    @Override
    public Long countActivityComment(String activityId) {
        return corgiCommentMapper.countActivityComment(activityId);
    }
}
