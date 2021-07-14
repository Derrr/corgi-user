package com.corgi.user.api;


import com.corgi.user.entity.InfluencerApply;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiInfluencerApplyService {
    void addApply(InfluencerApply apply);

    List<InfluencerApply> getApplies(Integer page, Integer size);
}
