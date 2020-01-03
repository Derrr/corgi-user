package com.corgi.mapper;

import com.corgi.user.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserTagMapper {
    List<String> getTags();

    List<String> getUserTag(@Param("userId") String userId);

    void deleteUserTag(@Param("userId") String userId);

    void addUserTag(@Param("userId") String userId, @Param("tag") String tag);

    List<String> getCategoryInterests(@Param("category") String category);

    List<UserInterest> getUserInterests(@Param("userId") String userId);

    void deleteUserInterests(@Param("userId") String userId, @Param("category") String category);

    void addUserInterests(@Param("userId") String userId, @Param("category") String category, @Param("interest") String interest);
}
