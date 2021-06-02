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

    void updateAllCorgiDate(@Param("date") CorgiDate date);

    void updateCorgiDateDetail(@Param("date") CorgiDate date);

    void addDateApply(@Param("apply") CorgiDateApply apply, @Param("date")CorgiDate date);

    void updateDateApply(@Param("apply") CorgiDateApply apply);

    void finishDateApply(@Param("apply") CorgiDateApply apply);

    void updateApplyProgress(@Param("apply") CorgiDateApply apply);

    List<CorgiDateApply> getApplyByProgress(@Param("userId")String userId, @Param("progress")String progress);

    void updateApplyDetail(@Param("apply") CorgiDateApply apply);

    CorgiDateApply getApplyById(@Param("id") Integer id);

    List<CorgiDateApply> getApplies(@Param("userId")String approvalUserId,@Param("start")Integer start, @Param("size")Integer size);

    List<CorgiDateApply> searchApplies(@Param("apply")CorgiDateApply apply,@Param("start")Integer start, @Param("size")Integer size);

    CorgiApplyCombind getCombind(@Param("userId")String approvalUserId ,@Param("startTime")String startTime, @Param("status")String status);

    List<String> getAvatars(@Param("userId")String approvalUserId ,@Param("startTime")String startTime, @Param("status")String status);

    CorgiDateApply getUserApply(@Param("userId") String userId, @Param("approvalId")String approvalUserId);

    List<CorgiDateApply> getApprovedApplies(@Param("userId")String approvalUserId, @Param("status")String status);

}
