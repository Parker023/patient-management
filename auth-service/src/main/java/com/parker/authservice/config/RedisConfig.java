package com.parker.authservice.config;

import com.parker.authservice.dto.PendingRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shanmukhaanirudhtalluri
 * @date 28/06/25
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, PendingRegistration> otpSenderRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, PendingRegistration> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use JSON serialization
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(PendingRegistration.class));

        return template;
    }
}
