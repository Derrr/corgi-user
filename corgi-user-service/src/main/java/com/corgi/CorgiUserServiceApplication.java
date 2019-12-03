package com.corgi;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Description:
 * @Author: liu.tr
 * @Date: 2019/12/1
 */
@MapperScan("com.corgi.mapper")
@SpringBootApplication
@EnableDubboConfiguration
public class CorgiUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorgiUserServiceApplication.class, args);
    }


}
