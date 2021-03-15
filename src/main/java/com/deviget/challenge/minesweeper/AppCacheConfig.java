package com.deviget.challenge.minesweeper;

import java.util.HashMap;
import java.util.Map;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude={ RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching(mode=AdviceMode.PROXY)
public class AppCacheConfig {
	static public final String SESSIONS_CACHE_REGION = "SESSIONS_CACHE_REGION";

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();

        // create cache sessions with ttl = 60 minutes and maxIdleTime = 15 minutes
        config.put(SESSIONS_CACHE_REGION, new CacheConfig(60*60*1000, 15*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
    
}
