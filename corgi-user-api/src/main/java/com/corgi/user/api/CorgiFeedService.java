package com.corgi.user.api;


import com.corgi.entity.ActivityQuery;
import com.corgi.user.entity.CorgiFeed;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiFeedService {
    List<String> getUnviewFeed(String userId, Integer size);

    List<String> searchFeed(ActivityQuery query);

    List<String> getFeedByActivityId(String activityId, String userId, Integer page, Integer size);

    Integer countUnviewFeed(String userId);

    void viewFeed(CorgiFeed feed);

    void addBarFeed(CorgiFeed corgiFeed);

    void addFeed(CorgiFeed corgiFeed);

    Integer countViewFeed(String date);

    void deleteFeed(CorgiFeed feed);
}
