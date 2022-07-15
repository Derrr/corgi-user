package com.corgi.user.api;

import com.corgi.activity.entity.CorgiActivity;
import com.corgi.entity.ActivityQuery;
import com.corgi.user.entity.*;

import java.util.List;

public interface CorgiBillboardService {
    List<UserProfile> getPastBillboard(String date);

    List<UserProfile> getBillboard(String date);

    void addBillboard(UserProfile userProfile, String date, String countType);

    void cleanBillboard(String date);

    List<UserProfile> getPopularUser(UserDetail userDetail, Integer limit);

    List<CorgiActivity> getPopularActivity(ActivityQuery activityQuery, Integer limit);

    List<UserProfile> getPassionUser(UserDetail userDetail, Integer limit);

    List<UserProfile> getActiveUser(UserDetail userDetail, Integer limit);

    void updateBillboardByNickname(String from, String to, String date);

    void updateBillboardOrder(String activityId, String date, Integer order);

    List<Billboard> getBillboardByDate(String startDate, String endDate);

    void addActivityBillboard(ActivityBillboard activityBillboard);

    void deleteActivityBillboard(ActivityBillboard activityBillboard);

    List<String> getActivityBillboard(String date);

    List<ActivityBillboard> getAllActivityBillboard(ActivityBillboard activityBillboard);

    Integer countOnBoard(String userId);

    PaidBillboard createPaidBillboard(PaidBillboard paidBillboard);

    void updatePaiBillboard(PaidBillboard paidBillboard);

    Integer countPaiBillboard(PaidBillboard paidBillboard);

    List<PaidBillboard> queryPaidBillboard(PaidBillboard paidBillboard, Integer page, Integer size);
}
