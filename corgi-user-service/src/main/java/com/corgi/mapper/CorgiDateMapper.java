package com.corgi.mapper;

import com.corgi.user.entity.CorgiApplyCombind;
import com.corgi.user.entity.CorgiDate;
import com.corgi.user.entity.CorgiDateApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiDateMapper {

    void addCorgiDate(@Param("date") CorgiDate date);

    List<CorgiDate> searchDate(@Param("date") CorgiDate date);

    CorgiDate getDateByUserId(@Param("userId") String userId);

    CorgiDate getDateById(@Param("id") String id);

    void updateCorgiDate(@Param("date") CorgiDate date);

    void addDateApply(@Param("apply") CorgiDateApply apply);

    void updateDateApply(@Param("apply") CorgiDateApply apply);

    CorgiDateApply getApplyById(@Param("id") Integer id);

    List<CorgiDateApply> getApplies(@Param("userId")String approvalUserId,@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("status")String status);

    CorgiApplyCombind getCombind(@Param("userId")String approvalUserId ,@Param("startTime")String startTime, @Param("status")String status);

    List<String> getAvatars(@Param("userId")String approvalUserId ,@Param("startTime")String startTime, @Param("status")String status);

    CorgiDateApply getUserApply(@Param("userId") String userId, @Param("approvalId")String approvalUserId);

    List<CorgiDateApply> getApprovedApplies(@Param("userId")String approvalUserId, @Param("status")String status);

}
