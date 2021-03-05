package com.corgi.user.api;

import com.corgi.user.entity.CorgiOpenPage;

import java.util.List;

public interface CorgiOpenPageService {
    void addOpenPage(CorgiOpenPage corgiOpenPage);

    void deleteOpenPage(Integer pageId);

    void updateOpenPage(CorgiOpenPage corgiOpenPage);

    List<CorgiOpenPage> listOpenPage(CorgiOpenPage corgiOpenPage);

    List<CorgiOpenPage> getBirthdayOpenPage(String userId);
}
