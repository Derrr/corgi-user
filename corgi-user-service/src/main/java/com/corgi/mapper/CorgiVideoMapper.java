package com.corgi.mapper;

import com.corgi.user.entity.UserVideo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVideoMapper {
    List<UserVideo> getVideo(@Param("userId") String userId);

    void addVideo(@Param("video") UserVideo userVideo);

    void deleteVideo(@Param("userId") String userId);
}
