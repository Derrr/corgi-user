package com.corgi.user.api;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CheckPic;
import com.corgi.user.entity.*;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiPicService {
    String addUserPic(UserPic userPic);

    String deleteUserPic(String picId);

    List<UserPic> getUserPic(String userId);

    String addActivityPic(ActivityPic activityPic);

    String deleteActivityPic(String picId);

    List<ActivityPic> getActivityPic(String activityId);

    String addCheckPic(CheckPic checkPic);

    String failCheckPic(CheckPic checkPic);

    String passCheckPic(CheckPic checkPic);

    List<CheckPic> getCheckPic(String status,String type, int page, int size);

    List<CheckPic> getCheckPicBySourceId(String type, String sourceId);

    long countCheckPic(String status,String type);

    String updateUserPic(UserPic userPic);
}
