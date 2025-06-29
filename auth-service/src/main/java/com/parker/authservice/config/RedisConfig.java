package com.parker.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        //we're using LocalDate in RegistrationRequest and Jackson2JsonRedisSerializer unable to deserialize
        // so we need to provide  custom mapper


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Jackson2JsonRedisSerializer<PendingRegistration> serializer =
                new Jackson2JsonRedisSerializer<>(mapper, PendingRegistration.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        return template;
    }
}
