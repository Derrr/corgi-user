package com.corgi.user.api;


import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.UserVideo;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVideoService {
    List<UserVideo> getVideo(String userId);

    void addVideo(UserVideo userVideo);

    void deleteVideo(String userId);
}
