package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiArea;
import com.corgi.mapper.CorgiAreaMapper;
import com.corgi.mapper.CorgiFeedMapper;
import com.corgi.mapper.CorgiUserMapper;
import com.corgi.mapper.CorgiVlogMapper;
import com.corgi.user.api.CorgiAreaService;
import com.corgi.user.api.CorgiFeedService;
import com.corgi.user.api.CorgiUserService;
import com.corgi.user.entity.CorgiFeed;
import com.corgi.user.entity.CorgiVlog;
import com.corgi.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiFeedService.class)
@Slf4j
@Component
public class CorgiFeedServiceImpl implements CorgiFeedService {

    @Autowired
    private CorgiFeedMapper corgiFeedMapper;

    @Autowired
    private CorgiUserMapper corgiUserMapper;

    @Autowired
    private CorgiVlogMapper corgiVlogMapper;

    @Override
    public List<String> getUnviewFeed(String userId) {
        List<String> result = corgiFeedMapper.getUnviewFeed(userId, UserUtils.getIndex(userId));
        if (result.size() > 0) {
            return result;
        }
        List<CorgiVlog> logList;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logList = corgiVlogMapper.getPopularVlog(userId, sdf.format(new Date()), UserUtils.getIndex(userId));

        for (CorgiVlog corgiVlog : logList) {
            corgiFeedMapper.addFeed(buildFeed(corgiVlog, userId), UserUtils.getIndex(userId));
        }
        return corgiFeedMapper.getUnviewFeed(userId, UserUtils.getIndex(userId));
    }

    @Override
    public void viewFeed(String userId, String feed) {
        corgiFeedMapper.viewFeed(userId, feed, UserUtils.getIndex(userId));
    }

    @Override
    public void addFeed(CorgiFeed corgiFeed) {
        if (corgiFeed != null && corgiFeed.hasValue()) {
            corgiFeedMapper.addFeed(corgiFeed, UserUtils.getIndex(corgiFeed.getUserId()));
        }
    }


    private CorgiFeed buildFeed(CorgiVlog vlog, String userId) {
        CorgiFeed feed = new CorgiFeed();
        feed.setFeed(vlog.getActivityId());
        feed.setFeedUserId(vlog.getUserId());
        feed.setUserId(userId);
        return feed;
    }

}
