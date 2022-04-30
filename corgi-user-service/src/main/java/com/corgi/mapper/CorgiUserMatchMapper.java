package com.corgi.mapper;

import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserMatch;
import com.corgi.user.entity.UserMatchItem;
import com.corgi.user.entity.UserQuery;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiUserMatchMapper {

    List<UserMatchItem> getMatchByTime(@Param("user")UserQuery userQuery, @Param("timestamp") Long timestamp, @Param("size") Integer size);

    void addMatch(@Param("userId") String userId, @Param("matchId") String matchId, @Param("tradeNo") String tradeNo);

    void addMatchView(@Param("userId") String userId, @Param("matchId") String matchId);

    void updateMatchViewByDate(@Param("date") String date);

    void updateMatchByDate(@Param("date") String date);

    Integer countMatchByRange(@Param("query")UserQuerySupporter supporter, @Param("timestamp")Long timestamp);

    Integer countMatch(@Param("userId") String userId, @Param("tradeNo") String tradeNo, @Param("date") String date);


    /**
     * 获取族类匹配
     *
     * @param group1
     * @param group2
     * @return
     */
    Integer getGroupMatch(@Param("group1") String group1, @Param("group2") String group2);

    /**
     * 获取性格匹配
     *
     * @param ctr1
     * @param ctr2
     * @return
     */
    Integer getCharacterMatch(@Param("ctr1") String ctr1, @Param("ctr2") String ctr2);

    /**
     * 获取号码牌匹配
     *
     * @param role1
     * @param role2
     * @return
     */
    Integer getRoleMatch(@Param("role1") String role1, @Param("role2") String role2);


    /**
     * 添加匹配缓存
     *
     * @param userId1
     * @param userId2
     * @param match
     */
    void addMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2, @Param("match") Double match);

    /**
     * 更新匹配缓存
     *
     * @param userId1
     * @param userId2
     * @param match
     */
    void updateMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2, @Param("match") Double match);

    /**
     * 清除缓存
     *
     * @param userId1
     * @param userId2
     */
    void deleteMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2);


    /**
     * 获取匹配缓存
     *
     * @param userId1
     * @param userId2
     */
    Double getMatchCache(@Param("userId1") String userId1, @Param("userId2") String userId2);

    /**
     * 批量获取匹配
     *
     * @param userId
     * @param start
     * @param size
     * @return
     */
    List<UserMatch> getMatchByPage(@Param("userId") String userId, @Param("start") int start, @Param("size") int size);

    /**
     * 获取匹配参数
     *
     * @param table
     * @return
     */
    List<HashMap> getMatchFactor(@Param("table") String table);

    /**
     * 更新匹配参数
     *
     * @param table
     * @param cn1
     * @param cv1
     * @param cn2
     * @param cv2
     * @param match
     */
    void updateMatchFactor(@Param("table") String table, @Param("cn1") String cn1, @Param("cv1") String cv1, @Param("cn2") String cn2, @Param("cv2") String cv2, @Param("match") Integer match);

}
