package com.corgi.user.api;

import com.corgi.user.entity.CorgiSound;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiSoundService {
    void addCorgiSound(CorgiSound corgiSound);

    void updateCorgiSound(CorgiSound corgiSound);

    void deleteCorgiSound(String userId);

    CorgiSound getCorgiSound(String userId);

    void failCheckSound(CorgiSound corgiSound);

    void passCheckSound(CorgiSound corgiSound);

    List<CorgiSound> getCheckSound(String status, int page, int size);

    long countCheckSound(String status);

    void updateCheckSound(CorgiSound corgiSound);
}
