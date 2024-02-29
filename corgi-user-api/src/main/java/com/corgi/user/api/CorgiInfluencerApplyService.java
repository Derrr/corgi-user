package com.corgi.user.api;


import com.corgi.user.entity.InfluencerApply;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiInfluencerApplyService {
    void addApply(InfluencerApply apply);

    void updateApply(InfluencerApply apply);

    List<InfluencerApply> getApplies(InfluencerApply apply, Integer page, Integer size);

    Integer countApplies(InfluencerApply apply);
}
