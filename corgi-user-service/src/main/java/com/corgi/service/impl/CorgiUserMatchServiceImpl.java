package com.corgi.service.impl;

import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiUserMatchMapper;
import com.corgi.support.MatchSupporter;
import com.corgi.user.api.CorgiUserMatchService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tairanliu
 */
public class CorgiUserMatchServiceImpl implements CorgiUserMatchService {
    @Autowired
    private CorgiUserMatchMapper userMatchMapper;
    @Autowired
    private CorgiUserMapper userMapper;

    @Override
    public Double getUserMatch(String userId1, String userId2) {
        UserDetail userDetail1 = userMapper.getUserDetail(userId1);
        UserDetail userDetail2 = userMapper.getUserDetail(userId2);
        return getUserMatchDetail(userDetail1, userDetail2);
    }

    @Override
    public Double getUserMatchDetail(UserDetail userDetail1, UserDetail userDetail2) {
        Double match = 0.0;
        match += MatchSupporter.getConMatch(userDetail1.getCon(), userDetail2.getCon());
        match += MatchSupporter.getRoleMatch(userDetail1.getRole(), userDetail2.getRole());
        match += MatchSupporter.getFactorMatch(userDetail1.getCharacter(), userDetail2.getCharacter());
        match += userMatchMapper.getCharacterMatch(userDetail1.getNatureCharacter(), userDetail2.getNatureCharacter()) * 0.1;
        match += userMatchMapper.getGroupMatch(userDetail1.getGroup(), userDetail2.getGroup()) * 0.45;
        return match;
    }
}
