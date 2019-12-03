package com.corgi.user.api;

import com.corgi.user.entity.UserLogin;

/**
 * @author tairanliu
 */
public interface CorgiUserService {
    String login(UserLogin userLogin);
}
