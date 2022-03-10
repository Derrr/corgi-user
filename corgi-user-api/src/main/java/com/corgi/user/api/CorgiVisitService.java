package com.corgi.user.api;


import com.corgi.user.entity.UserProfile;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVisitService {
    void visit(String userId, String toId);

    List<UserProfile> getVisitor(String userId,Integer page, Integer limit);

    List<UserProfile> getVisited(String userId,Integer page, Integer limit);

    List<UserProfile> getVisitedByCount(String userId,Integer page, Integer limit);

    List<UserProfile> getVisitorByCount(String userId,Integer page, Integer limit);

    Integer countVisit(String userId);

    Integer countVisitUnread(String userId);
}
