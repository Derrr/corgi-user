package com.corgi.mapper;

import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVlogMapper {
    void addVlog(@Param("vlog") CorgiVlog corgiVlog);

    void deleteVlogByActivityId(@Param("activityId") String activityId);

    List<CorgiVlog> getInitVlog(@Param("userId") String userId, @Param("ctime") String ctime);

    List<CorgiVlog> getPopularVlog(@Param("userId") String userId, @Param("ctime") String ctime, @Param("index") String index);

    CorgiVlog getVlogById(@Param("activityId") String activityId);

    void deleteVlog(@Param("activityId") String activityId);

    void addVlogCount(@Param("vlog") CorgiVlog corgiVlog);

    List<CorgiVlog> recallVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<CorgiVlog> recallTargetVlog(@Param("targetId") String userId, @Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<CorgiVlog> recallHotVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<CorgiVlog> getFollowVlog(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size, @Param("ctime") String ctime);

    List<CorgiVlog> getUserVlog(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size, @Param("ctime") String ctime);

    void failVlog(@Param("activityId") String activityId);

    Integer countVlog();

    String selectOne(@Param("start") Integer start);

    void addVlogHot(@Param("vlog") CorgiVlogHot hot);

    void updateVlogHot(@Param("vlog") CorgiVlogHot hot);

    List<CorgiVlogHot> getVlogHot(@Param("vlog") CorgiVlogHot hot);
}
