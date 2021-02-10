package com.corgi.user.api;


import com.corgi.user.entity.UserProfile;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVisitService {
    void visit(String userId, String toId);

    List<UserProfile> getVisitor(String userId, Integer limit);
}
