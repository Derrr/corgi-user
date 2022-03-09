package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiVisitMapper {
    void addVisit(@Param("userId") String userId, @Param("visitorId") String visitorId);

    void addVisitCount(@Param("userId") String userId, @Param("visitorId") String visitorId);

    List<UserProfile> getVisitor(@Param("userId") String userId, @Param("limit") Integer limit);

    List<UserProfile> getVisited(@Param("userId") String userId, @Param("limit") Integer limit);

    List<UserProfile> getVisitorByCount(@Param("userId") String userId, @Param("limit") Integer limit);

    List<UserProfile> getVisitedByCount(@Param("userId") String userId, @Param("limit") Integer limit);

    Integer countVisit(@Param("userId") String userId);

    Integer countVisitUnread(@Param("userId") String userId);

    void readVisit(@Param("userId")String userId);
}
