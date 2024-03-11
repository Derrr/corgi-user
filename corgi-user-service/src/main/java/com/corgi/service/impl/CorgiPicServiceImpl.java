package com.corgi.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.api.CorgiActivityService;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.common.CorgiConstants;
import com.corgi.entity.CheckPic;
import com.corgi.entity.CorgiPic;
import com.corgi.mapper.CorgiPicMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiPicService.class)
@Slf4j
@Component
public class CorgiPicServiceImpl implements CorgiPicService {
    @Autowired
    private CorgiPicMapper corgiPicMapper;
    @Autowired
    private CorgiUserMapper corgiUserMapper;

    private static String SUFFIX = "?x-oss-process=style/mask";

    @Override
    public String addUserPic(UserPic userPic) {
        corgiPicMapper.addUserPic(userPic);
        return userPic.getPicId();
    }

    @Override
    public String deleteUserPic(String picId, String userId) {
        corgiPicMapper.deleteUserPic(picId, userId);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<UserPic> getUserPic(String userId) {
        List<UserPic> userPics = corgiPicMapper.getUserPic(userId);
        return userPics;
    }

    @Override
    public String addActivityPic(ActivityPic activityPic) {
        corgiPicMapper.addActivityPic(activityPic);
        return activityPic.getPicId();
    }

    @Override
    public String deleteActivityPic(String picId) {
        corgiPicMapper.deleteActivityPic(picId);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<ActivityPic> getActivityPic(String activityId) {
        List<ActivityPic> activityPics = corgiPicMapper.getActivityPic(activityId);
        if (activityPics != null) {
            activityPics.stream().forEach(pic -> addSuffix(pic));
        }
        return activityPics;
    }

    @Override
    public String addCheckPic(CheckPic checkPic) {
        corgiPicMapper.addCheckPic(checkPic);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String failCheckPic(CheckPic checkPic) {
        if (CheckPic.ACTIVITY.equals(checkPic.getType())) {
            corgiPicMapper.deleteActivityPicByDataId(checkPic.getDataId());
        } else if (CheckPic.USER.equals(checkPic.getType())) {
            corgiPicMapper.deleteUserPicByDataId(checkPic.getDataId());
        } else if (CheckPic.AVATAR.equals(checkPic.getType())) {
            corgiUserMapper.deleteUserAvatar(checkPic.getDataId());
        } else if (CheckPic.BACKGROUND.equals(checkPic.getType())) {
            corgiUserMapper.updateUserBackground(checkPic.getDataId(), CheckPic.NORMAL, null);
        } else {
            return "no type matches";
        }
        corgiPicMapper.updateCheckPic(checkPic.getDataId(), CorgiPic.FAIL, checkPic.getUserId());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String noFaceCheckPic(CheckPic checkPic) {
        corgiUserMapper.updateUserAvatar(checkPic.getUserId(), checkPic.getDataId(), UserDetail.NO_FACE, checkPic.getPicUrl());
        corgiPicMapper.updateCheckPic(checkPic.getDataId(), UserDetail.NO_FACE, checkPic.getUserId());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String passCheckPic(CheckPic checkPic) {
        checkPic = corgiPicMapper.getCheckPicByDataId(checkPic.getDataId());
        if (CheckPic.ACTIVITY.equals(checkPic.getType()) || "paying".equals(checkPic.getType())) {
            corgiPicMapper.updateActivityPicByDataId(checkPic.getDataId(), CorgiPic.NORMAL);
        } else if (CheckPic.USER.equals(checkPic.getType())) {
            corgiPicMapper.updateUserPicByDataId(checkPic.getDataId(), CorgiPic.NORMAL);
        } else if (CheckPic.AVATAR.equals(checkPic.getType())) {
            corgiUserMapper.updateUserAvatar(checkPic.getUserId(), checkPic.getDataId(), checkPic.getPicUrl(), CorgiPic.NORMAL);
        } else if (CheckPic.BACKGROUND.equals(checkPic.getType())) {
            corgiUserMapper.updateUserBackground(checkPic.getDataId(), CorgiPic.NORMAL, checkPic.getPicUrl());
        } else {
            return "no type matches";
        }
        corgiPicMapper.updateCheckPic(checkPic.getDataId(), CorgiPic.NORMAL, checkPic.getUserId());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<CheckPic> getCheckPic(String userId, String status, String type, int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size <= 0) {
            size = 20;
        }
        List<CheckPic> checkPics = corgiPicMapper.getCheckPic(userId, status, type, (page - 1) * size, size);
        if (!CollectionUtils.isEmpty(checkPics)) {
            for (CheckPic checkPic : checkPics) {
                if (!StringUtils.isEmpty(checkPic.getUserId())) {
                    continue;
                }
                if (CheckPic.USER.equals(checkPic.getType())) {
                    UserPic userPic = corgiPicMapper.getUserPicByDataId(checkPic.getDataId());
                    if (userPic != null) {
                        checkPic.setUserId(userPic.getUserId());
                    }
                } else if (CheckPic.ACTIVITY.equals(checkPic.getType())) {
                    ActivityPic activityPic = corgiPicMapper.getActivityPicByDataId(checkPic.getDataId());
                    if (activityPic != null) {
                        checkPic.setUserId(activityPic.getActivityId());
                    }
                }
            }
        }
        return checkPics;
    }

    @Override
    public List<CheckPic> getCheckPicBySourceId(String type, String sourceId) {
        return corgiPicMapper.getCheckPicBySourceId(type, sourceId);
    }

    @Override
    public CheckPic getCheckPicByDataId(String dataId) {
        return corgiPicMapper.getCheckPicByDataId(dataId);
    }

    @Override
    public long countCheckPic(String status, String type, String userId) {
        long count = corgiPicMapper.countCheckPic(status, type, userId);
        return count;
    }

    @Override
    public String updateUserPic(UserPic userPic) {
        corgiPicMapper.updateUserPic(userPic);
        return CorgiConstants.SUCCESS;
    }

    private CorgiPic addSuffix(CorgiPic corgiPic) {
        if (corgiPic == null || StringUtils.isEmpty(corgiPic.getPicUrl())) {
            return null;
        }
        corgiPic.setPicUrl(corgiPic.getPicUrl().replaceAll("corgi-pic\\.oss-cn-beijing\\.aliyuncs\\.com", "image.corgi.org.cn").replaceAll("https://","http://"));
        return corgiPic;
    }

}
