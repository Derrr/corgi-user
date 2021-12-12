package com.corgi.mapper;

import com.corgi.user.entity.InfluencerApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiInfluencerApplyMapper {
    void addApply(@Param("apply") InfluencerApply apply);

    List<InfluencerApply> getApplies(@Param("start") Integer start, @Param("size") Integer size);
}
