package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.entity.CorgiPic;
import com.corgi.mapper.CorgiSoundMapper;
import com.corgi.user.api.CorgiSoundService;
import com.corgi.user.entity.CorgiSound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Service(interfaceClass = CorgiSoundService.class)
@Slf4j
@Component
public class CorgiSoundSerivce implements CorgiSoundService {
    @Autowired
    private CorgiSoundMapper corgiSoundMapper;

    @Override
    public void addCorgiSound(CorgiSound corgiSound) {
        corgiSoundMapper.addCorgiSound(corgiSound);
    }

    @Override
    public void deleteCorgiSound(String userId) {
        corgiSoundMapper.deleteCorgiSound(userId);
    }

    @Override
    public CorgiSound getCorgiSound(String userId) {
        List<CorgiSound> corgiSounds = corgiSoundMapper.getCorgiSound(userId);
        if (corgiSounds.size() > 0) {
            return corgiSounds.get(corgiSounds.size() - 1);
        }
        return null;
    }

    @Override
    public void failCheckSound(CorgiSound corgiSound) {
        corgiSoundMapper.updateCorgiSoundByDataId(corgiSound.getDataId(), CorgiPic.FAIL);
    }

    @Override
    public void passCheckSound(CorgiSound corgiSound) {
        corgiSoundMapper.updateCorgiSoundByDataId(corgiSound.getDataId(), CorgiPic.NORMAL);
    }

    @Override
    public List<CorgiSound> getCheckSound(String status, int page, int size) {
        return corgiSoundMapper.getCheckSound(status, (page - 1) * size, size);
    }

    @Override
    public long countCheckSound(String status) {
        return corgiSoundMapper.countCheckSound(status);
    }

    @Override
    public void updateCheckSound(CorgiSound corgiSound) {
        corgiSoundMapper.updateCorgiSoundByDataId(corgiSound.getDataId(), corgiSound.getStatus());
    }
}
