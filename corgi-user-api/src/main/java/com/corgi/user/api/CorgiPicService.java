package com.corgi.user.api;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiPicService {
    String addUserPic(UserPic userPic);

    String deleteUserPic(String picId);

    String addActivityPic(ActivityPic activityPic);

    String deleteActivityPic(String picId);

    List<ActivityPic> getActivityPic(String activityId);
}
