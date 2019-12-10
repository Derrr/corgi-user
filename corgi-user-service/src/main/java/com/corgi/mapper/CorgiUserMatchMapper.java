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
}
