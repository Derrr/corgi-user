package com.corgi.user.api;

import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;

import java.util.List;

public interface CorgiBillboardService {
    List<UserProfile> getPastBillboard(String date);

    List<UserProfile> getBillboard(String date);

    void addBillboard(UserProfile userProfile, String date, String countType);

    void cleanBillboard(String date);

    List<UserProfile> getPopularUser(UserDetail userDetail, Integer limit);

    List<UserProfile> getPassionUser(UserDetail userDetail, Integer limit);

    List<UserProfile> getActiveUser(UserDetail userDetail, Integer limit);

    void updateBillboardByNickname(String from, String to, String date);
}
