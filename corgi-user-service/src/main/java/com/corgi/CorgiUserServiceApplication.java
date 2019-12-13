package com.corgi;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.corgi.common.CorgiQueueName;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


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

    @Bean
    public Queue refreshMatchQueue() {
        return new Queue(CorgiQueueName.REFRESH_MATCH_QUEUE);
    }
}
