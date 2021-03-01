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
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        if (result.size() >= 5) {
            return result;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<CorgiVlog> logList = corgiVlogMapper.getPopularVlog(userId, sdf.format(new Date()), UserUtils.getIndex(userId));

        if (logList.size() > 0) {
            for (CorgiVlog corgiVlog : logList) {
                if (corgiVlog != null && !StringUtils.isEmpty(corgiVlog.getActivityId()) && !result.contains(corgiVlog.getActivityId())) {
                    corgiFeedMapper.addFeed(buildFeed(corgiVlog, userId), UserUtils.getIndex(userId));
                    result.add(corgiVlog.getActivityId());
                }
            }
        }
        if (result.size() < 5) {
            Integer max = 20 - result.size();
            Integer total = corgiVlogMapper.countVlog();
            Random random = new Random();
            result = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                CorgiVlog vlog = corgiVlogMapper.selectOne(random.nextInt(total));
                if (vlog != null && !StringUtils.isEmpty(vlog.getActivityId())
                        && !result.contains(vlog.getActivityId())) {
                    result.add(vlog.getActivityId());
                    corgiFeedMapper.addFeed(buildFeed(vlog, userId), UserUtils.getIndex(userId));
                }
            }
        }
        return result;
    }

    @Override
    public Integer countUnviewFeed(String userId) {
        return corgiFeedMapper.countUnviewFeed(userId, UserUtils.getIndex(userId));
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

    @Override
    public Integer countViewFeed(String date) {
        Integer total = 0;
        for (int i = 0; i < 8; i++) {
            total += corgiFeedMapper.countViewFeed(date, i + "");
        }
        return total;
    }


    private CorgiFeed buildFeed(CorgiVlog vlog, String userId) {
        CorgiFeed feed = new CorgiFeed();
        feed.setFeed(vlog.getActivityId());
        feed.setFeedUserId(vlog.getUserId());
        feed.setUserId(userId);
        return feed;
    }

}
