package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiBannerMapper;
import com.corgi.user.api.CorgiBannerService;
import com.corgi.user.entity.CorgiBanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiBannerService.class)
@Slf4j
@Component
public class CorgiBannerServiceImpl implements CorgiBannerService {
    @Autowired
    private CorgiBannerMapper corgiBannerMapper;

    @Override
    public void addBanner(CorgiBanner corgiBanner) {
        corgiBannerMapper.addBanner(corgiBanner);
    }

    @Override
    public void deleteBanner(Integer bannerId) {
        corgiBannerMapper.deleteBanner(bannerId);
    }

    @Override
    public void updateBanner(CorgiBanner corgiBanner) {
        corgiBannerMapper.updateBanner(corgiBanner);
    }

    @Override
    public List<CorgiBanner> listBanner(CorgiBanner corgiBanner) {
        return corgiBannerMapper.listBanner(corgiBanner);
    }
}
