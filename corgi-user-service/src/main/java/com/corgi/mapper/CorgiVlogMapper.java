package com.corgi.mapper;

import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.user.entity.CorgiVlogHot;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVlogMapper {
    void addVlog(@Param("vlog") CorgiVlog corgiVlog);

    void deleteVlogByActivityId(@Param("activityId") String activityId);

    List<CorgiVlog> getInitVlog(@Param("userId") String userId, @Param("ctime") String ctime);

    List<CorgiVlog> getPopularVlog(@Param("userId") String userId, @Param("index") String index, @Param("size") Integer size);

    CorgiVlog getVlogById(@Param("videoId") String videoId);

    CorgiVlog getVlogByActivityId(@Param("activityId") String activityId);

    void deleteVlog(@Param("activityId") String activityId);

    void addVlogCount(@Param("vlog") CorgiVlog corgiVlog);

    List<CorgiVlog> recallBarVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size);

    List<CorgiVlog> recallVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("city") String city, @Param("index") String index);

    List<CorgiVlog> recallLikeVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<CorgiVlog> recallTargetVlog(@Param("targetId") String userId, @Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<String> recallActivityVlog(@Param("activityId") String activityId, @Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    Integer countActivityVlog(@Param("activityId") String activityId, @Param("userId") String userId);

    List<String> recallByActivityId(@Param("activityId") String activityId, @Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size);

    List<CorgiVlog> recallHotVlog(@Param("vlog") CorgiVlog corgiVlog, @Param("size") Integer size, @Param("index") String index);

    List<CorgiVlog> getFollowVlog(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size, @Param("ctime") String ctime);

    List<CorgiVlog> getUserVlog(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size, @Param("ctime") String ctime);

    List<CorgiVlog> getTopicVlog(@Param("userId") String userId, @Param("start") Integer start, @Param("size") Integer size, @Param("ctime") String ctime);

    void failVlog(@Param("activityId") String activityId);

    Integer countVlog();

    CorgiVlog selectOne(@Param("start") Integer start);

    String selectOneHot(@Param("start") Integer start);

    void addVlogHot(@Param("vlog") CorgiVlogHot hot);

    void updateVlogHot(@Param("vlog") CorgiVlogHot hot);

    List<CorgiVlogHot> getVlogHot(@Param("vlog") CorgiVlogHot hot, @Param("start") Integer start, @Param("size") Integer size);

    Integer countVlogHot(@Param("vlog") CorgiVlogHot hot);

    Integer countVlogByDate(@Param("date") String date);

    CorgiVlog countByTopic(@Param("topic") String topic);

    Integer countByHashtag(@Param("hashtagId") String hashtagId);

    Integer countLikeByHashtag(@Param("hashtagId") String hashtagId, @Param("type") String type);

    Integer countCommentByHashtag(@Param("hashtagId") String hashtagId);

    Integer countViewByHashtag(@Param("hashtagId") String hashtagId, @Param("index") String index);

}
