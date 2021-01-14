package com.corgi.user.api;


import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFakeService {
    void refreshFakeUser(Integer size);

    String selectFakeUser();
}
