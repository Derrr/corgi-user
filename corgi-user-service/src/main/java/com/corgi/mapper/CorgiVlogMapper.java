package com.corgi.mapper;

import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
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
}
