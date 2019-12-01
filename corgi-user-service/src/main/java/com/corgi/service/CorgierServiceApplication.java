package com.corgi.service;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Description:
 * @Author: liu.tr
 * @Date: 2019/12/1
 */
@SpringBootApplication
@EnableDubboConfiguration
public class CorgierServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorgierServiceApplication.class, args);
    }


}
