package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.activity.entity.ActivityPic;
import com.corgi.common.CorgiConstants;
import com.corgi.mapper.CorgiPicMapper;
import com.corgi.user.api.CorgiPicService;
import com.corgi.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


}
