package com.corgi.mapper;

/**
 * @author tairanliu
 */
public interface CorgiUserMatchMapper {
    /**
     * 获取族类匹配
     * @param group1
     * @param group2
     * @return
     */
    Integer getGroupMatch(String group1, String group2);

    /**
     * 获取性格匹配
     * @param ctr1
     * @param ctr2
     * @return
     */
    Integer getCharacterMatch(String ctr1, String ctr2);

    /**
     * 添加匹配缓存
     * @param userId1
     * @param userId2
     * @param match
     */
    void addMatchCache(String userId1,String userId2, Double match);

    /**
     * 更新匹配缓存
     * @param userId1
     * @param userId2
     * @param match
     */
    void updateMatchCache(String userId1,String userId2, Double match);

    /**
     * 获取匹配缓存
     * @param userId1
     * @param userId2
     */
    void getMatchCache(String userId1,String userId2);
}
