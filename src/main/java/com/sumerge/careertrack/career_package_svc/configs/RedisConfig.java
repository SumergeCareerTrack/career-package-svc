package com.sumerge.careertrack.learnings_svc.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String userScoresCacheHost;

    @Value("${spring.data.redis.port}")
    private int userScoresCachePort;

    @Bean
    public LettuceConnectionFactory userTokensConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();

        redisConfig.setHostName(userScoresCacheHost);
        redisConfig.setPort(userScoresCachePort);
        redisConfig.setDatabase(0);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public LettuceConnectionFactory userScoresCacheConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();

        redisConfig.setHostName(userScoresCacheHost);
        redisConfig.setPort(userScoresCachePort);
        redisConfig.setDatabase(1);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, String> userTokensTemplate(
            LettuceConnectionFactory userTokensConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(userTokensConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<Object, Object> userScoresTemplate(
            LettuceConnectionFactory userScoresCacheConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(userScoresCacheConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

}
