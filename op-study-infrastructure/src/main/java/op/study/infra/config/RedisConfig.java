package op.study.infra.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author xxs
 * @Date 2024/12/19
 * Redis配置类
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 缓存配置属性
     */
    @ConfigurationProperties(prefix = "cache.redis")
    public static class CacheProperties {
        private String keyPrefix = "user:domain:";
        private long defaultTtl = 600;
        private long userTtl = 1800;
        private long delayDeleteInterval = 500;
        private long lockTimeout = 10;

        // getters and setters
        public String getKeyPrefix() { return keyPrefix; }
        public void setKeyPrefix(String keyPrefix) { this.keyPrefix = keyPrefix; }
        public long getDefaultTtl() { return defaultTtl; }
        public void setDefaultTtl(long defaultTtl) { this.defaultTtl = defaultTtl; }
        public long getUserTtl() { return userTtl; }
        public void setUserTtl(long userTtl) { this.userTtl = userTtl; }
        public long getDelayDeleteInterval() { return delayDeleteInterval; }
        public void setDelayDeleteInterval(long delayDeleteInterval) { this.delayDeleteInterval = delayDeleteInterval; }
        public long getLockTimeout() { return lockTimeout; }
        public void setLockTimeout(long lockTimeout) { this.lockTimeout = lockTimeout; }
    }

    @Bean
    @ConfigurationProperties(prefix = "cache.redis")
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        
        // 添加 Java 8 时间模块支持
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        log.info("RedisTemplate配置完成");
        return template;
    }

    /**
     * 缓存管理器配置
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(cacheProperties.getDefaultTtl()))
                .computePrefixWith(cacheName -> cacheProperties.getKeyPrefix() + cacheName + ":")
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();

        log.info("RedisCacheManager配置完成，默认TTL: {}秒", cacheProperties.getDefaultTtl());
        return cacheManager;
    }
} 