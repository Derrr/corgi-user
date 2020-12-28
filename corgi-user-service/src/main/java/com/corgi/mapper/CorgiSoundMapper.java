package com.corgi.mapper;

import com.corgi.activity.entity.ActivityPic;
import com.corgi.entity.CheckPic;
import com.corgi.user.entity.CorgiSound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiSoundMapper {
    /**
     * 添加用户声音
     *
     * @param corgiSound
     */
    void addCorgiSound(@Param("corgiSound") CorgiSound corgiSound);

    /**
     * 删除用户声音
     *
     * @param userId
     */
    void deleteCorgiSound(@Param("userId") String userId);

    /**
     * 修改用户声音
     *
     * @param corgiSound
     */
    void updateCorgiSound(@Param("corgiSound") CorgiSound corgiSound);

    /**
     * 删除用户声音
     *
     * @param dataId
     */
    void deleteCorgiSoundByDataId(@Param("dataId") String dataId);

    /**
     * 修改用户声音状态
     *
     * @param dataId
     * @param status
     */
    void updateCorgiSoundByDataId(@Param("dataId") String dataId, @Param("status") String status);

    /**
     * 根据DataId获取用户声音
     * @param dataId
     * @return
     */
    CorgiSound getCorgiSoundByDataId(@Param("dataId") String dataId);

    /**
     * 获取用户声音
     *
     * @param userId
     * @return
     */
    List<CorgiSound> getCorgiSound(@Param("userId") String userId);

    /**
     * 审核图片计数
     *
     * @param status
     * @return
     */
    long countCheckSound(@Param("status") String status);

    /**
     * 获取审核图片
     *
     * @param status
     * @param start
     * @param size
     * @return
     */
    List<CorgiSound> getCheckSound(@Param("status") String status, @Param("start") long start, @Param("size") int size);

}
