package com.corgi.user.api;


import com.corgi.user.entity.CorgiFeed;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFeedService {
    List<String> getUnviewFeed(String userId, Integer size);

    Integer countUnviewFeed(String userId);

    void viewFeed(String userId, String feed);

    void addFeed(CorgiFeed corgiFeed);

    Integer countViewFeed(String date);
}
