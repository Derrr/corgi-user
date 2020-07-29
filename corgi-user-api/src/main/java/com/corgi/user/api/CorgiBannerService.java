package com.corgi.user.api;

import com.corgi.user.entity.CorgiBanner;

import java.util.List;

public interface CorgiBannerService {
    void addBanner(CorgiBanner corgiBanner);

    void deleteBanner(Integer bannerId);

    void updateBanner(CorgiBanner corgiBanner);

    List<CorgiBanner> listBanner(CorgiBanner corgiBanner);
}
