package com.corgi;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.corgi.common.CorgiQueueName;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Description:
 * @Author: liu.tr
 * @Date: 2019/12/1
 */
@MapperScan("com.corgi.mapper")
@SpringBootApplication
@EnableDubboConfiguration
@EnableTransactionManagement
public class CorgiUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorgiUserServiceApplication.class, args);
    }

    @Bean
    public Queue refreshMatchQueue() {
        return new Queue(CorgiQueueName.REFRESH_MATCH_QUEUE);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        //设置完这个可以直接将对象以json格式存入redis中，但是取出来的时候要用JSON.parseArray(Json.toJsonString(object),Object.class)解析一下
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;

    }
}
