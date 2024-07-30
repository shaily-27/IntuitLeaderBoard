package com.shaily.intuitLeaderboard.configuration;

import com.shaily.intuitLeaderboard.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Bean
    public Jedis jedis() {
        return new Jedis(new HostAndPort(Constants.REDIS_HOST, Constants.REDIS_PORT));
    }
}
