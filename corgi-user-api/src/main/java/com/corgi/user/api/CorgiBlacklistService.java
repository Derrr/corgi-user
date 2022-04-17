package com.corgi.user.api;

import com.corgi.user.entity.CorgiReport;
import com.corgi.user.entity.UserBasic;
import com.corgi.user.entity.UserProfile;

import java.util.List;

public interface CorgiBlacklistService {
    String addBlacklist(String userId, String blackId);

    String deleteBlacklist(String userId, String blackId);

    List<UserBasic> getBlackUser(String userId);

    void report(CorgiReport report);

    void updateStatus(String reportId, String status,String result);

    List<CorgiReport> getReport(CorgiReport report, Integer page, Integer size);

    Integer countReport(CorgiReport report);

    List<String> getBeBlacked(String userId);

    Integer isBlacked(String userId, String targetUserId);
}
