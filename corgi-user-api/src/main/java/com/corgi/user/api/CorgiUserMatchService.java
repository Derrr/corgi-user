package com.corgi.user.api;

import com.corgi.user.entity.UserDetail;

/**
 * @author tairanliu
 */
public interface CorgiUserMatchService {
    Double getUserMatch(String userId1, String userId2);

    Double getUserMatchDetail(UserDetail userDetail1, UserDetail userDetail2);
}
