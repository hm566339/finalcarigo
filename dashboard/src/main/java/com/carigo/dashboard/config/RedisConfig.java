package com.carigo.dashboard.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import com.carigo.dashboard.dto.OwnerDashboardResponse;

@Configuration
public class RedisConfig {

    /**
     * 🔹 Cache abstraction (@Cacheable)
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * 🔹 Manual Redis access (Dashboard service)
     */
    @Bean
    public RedisTemplate<String, OwnerDashboardResponse> ownerDashboardRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, OwnerDashboardResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key = String
        template.setKeySerializer(new StringRedisSerializer());

        // Value = JSON
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
