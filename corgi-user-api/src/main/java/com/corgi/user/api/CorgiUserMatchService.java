package com.corgi.user.api;

import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserMatch;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserMatchService {

    Double calculateUserMatch(String userId1, String userId2);

    Double calculateUserMatchByDetail(UserDetail userDetail1, UserDetail userDetail2);

    List<UserMatch> getUserMatchByPage(String userId, int start, int size);

    void updateMatch(UserMatch userMatch);

    Double getUserMatch(String userId1, String userId2);

    void clearMatch();

    List<HashMap> getMatchFactor(String table);
}
