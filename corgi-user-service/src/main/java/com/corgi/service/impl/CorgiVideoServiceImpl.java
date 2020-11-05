package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiVideoMapper;
import com.corgi.user.api.CorgiVideoService;
import com.corgi.user.entity.UserVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiVideoService.class)
@Slf4j
@Component
public class CorgiVideoServiceImpl implements CorgiVideoService {
    @Autowired
    private CorgiVideoMapper corgiVideoMapper;

    @Override
    public List<UserVideo> getVideo(String userId) {
        return corgiVideoMapper.getVideo(userId);
    }

    @Override
    public void addVideo(UserVideo userVideo) {
        corgiVideoMapper.addVideo(userVideo);
    }

    @Override
    public void deleteVideo(String userId) {
        corgiVideoMapper.deleteVideo(userId);
    }
}
