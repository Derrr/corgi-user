package com.corgi.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @Description:
 * @Author: liu.tr
 * @Date: 2019/12/1
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
@PropertySource("classpath:application.properties")
public class CorgierServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorgierServiceApplication.class, args);
    }


}
