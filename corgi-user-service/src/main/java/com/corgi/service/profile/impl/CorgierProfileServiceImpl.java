package com.corgi.service.profile.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.user.api.profile.service.CorgierProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tairanliu
 */
@Service(interfaceClass=CorgierProfileService.class)
@Slf4j
@Component
public class CorgierProfileServiceImpl implements CorgierProfileService {
    @Override
    public String register() {
        log.info("into the service");
        return "test";
    }
}
