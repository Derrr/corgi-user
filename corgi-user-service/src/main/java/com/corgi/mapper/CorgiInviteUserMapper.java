package com.corgi.mapper;

import com.corgi.entity.CorgiArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiInviteUserMapper {

    Integer addInvite(@Param("userId")String userId, @Param("inviteId")String inviteId, @Param("inviteTel")String inviteTel);

    Integer countInvite(@Param("userId") String userId);

    Integer countInviteTel(@Param("tel") String inviteTel);
}
