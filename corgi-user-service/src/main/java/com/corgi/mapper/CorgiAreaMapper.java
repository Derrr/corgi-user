package com.corgi.mapper;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CheckPic;
import com.corgi.entity.CorgiArea;
import com.corgi.user.entity.UserPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiAreaMapper {

    /**
     * 获取区域
     *
     * @param corgiArea
     * @return
     */
    List<CorgiArea> getArea(@Param("area") CorgiArea corgiArea);

    /**
     * 添加区域
     * @param corgiArea
     */
    void addArea(@Param("area") CorgiArea corgiArea);

    /**
     * 获取城市列表
     * @return
     */
    List<String> getCity();

}
