package com.corgi.user.api;


import com.corgi.entity.CorgiArea;

import java.util.List;

/**
 * @author tairanliu
 */
public interface CorgiAreaService {
    List<CorgiArea> getAreaByCity(String city);

    List<CorgiArea> getAreaByType(String city, String type);

    void addArea(CorgiArea corgiArea);
}
