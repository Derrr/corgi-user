package com.corgi.mapper;

import com.corgi.user.entity.UserMatch;
import com.corgi.user.entity.UserProfile;
import com.corgi.user.entity.UserSignUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserActivityMapper {
    /**
     * 添加报名
     *
     * @param userSignUp
     */
    void addSignUp(@Param("userSignUp") UserSignUp userSignUp);

    /**
     * 修改报名人状态
     *
     * @param userSignUp
     */
    void updateSignUp(@Param("userSignUp") UserSignUp userSignUp);

    List<UserProfile> getUser(@Param("activityId") String activityId);
}
