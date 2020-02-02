package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.common.CorgiConstants;
import com.corgi.entity.CheckPic;
import com.corgi.entity.CorgiPic;
import com.corgi.mapper.CorgiPicMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.entity.*;
import com.sun.tools.javac.comp.Check;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

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

    @Override
    public String addUserPic(UserPic userPic) {
        corgiPicMapper.addUserPic(userPic);
        return userPic.getPicId();
    }

    @Override
    public String deleteUserPic(String picId) {
        corgiPicMapper.deleteUserPic(picId);
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<UserPic> getUserPic(String userId) {
        return corgiPicMapper.getUserPic(userId);
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
        return corgiPicMapper.getActivityPic(activityId);
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
        } else {
            return "no type matches";
        }
        corgiPicMapper.updateCheckPic(checkPic.getDataId(), CorgiPic.FAIL, checkPic.getUserId());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public String passCheckPic(CheckPic checkPic) {
        if (CheckPic.ACTIVITY.equals(checkPic.getType())) {
            corgiPicMapper.updateActivityPicByDataId(checkPic.getDataId(), CorgiPic.NORMAL);
        } else if (CheckPic.USER.equals(checkPic.getType())) {
            corgiPicMapper.updateUserPicByDataId(checkPic.getDataId(), CorgiPic.NORMAL);
        } else if (CheckPic.AVATAR.equals(checkPic.getType())) {
            corgiUserMapper.updateUserAvatar(checkPic.getDataId(), CorgiPic.NORMAL);
        } else {
            return "no type matches";
        }
        corgiPicMapper.updateCheckPic(checkPic.getDataId(), CorgiPic.NORMAL, checkPic.getUserId());
        return CorgiConstants.SUCCESS;
    }

    @Override
    public List<CheckPic> getCheckPic(String status, String type, int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size <= 0) {
            size = 20;
        }
        List<CheckPic> checkPics = corgiPicMapper.getCheckPic(status, type, (page - 1) * size, size);
        if (!CollectionUtils.isEmpty(checkPics)) {
            for (CheckPic checkPic : checkPics) {
                if (CheckPic.USER.equals(checkPic.getType())) {
                    UserPic userPic = corgiPicMapper.getUserPicByDataId(checkPic.getDataId());
                    checkPic.setUserId(userPic.getUserId());
                }
            }
        }
        return checkPics;
    }

    @Override
    public long countCheckPic(String status, String type) {
        return corgiPicMapper.countCheckPic(status, type);
    }

    @Override
    public String updateUserPic(UserPic userPic) {
        corgiPicMapper.updateUserPic(userPic);
        return CorgiConstants.SUCCESS;
    }


}
