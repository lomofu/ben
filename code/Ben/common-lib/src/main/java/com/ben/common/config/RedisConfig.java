package com.ben.common.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author lomofu
 * @date 2020/2/27 21:26
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

  @Resource RedisConnectionFactory redisConnectionFactory;

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    StringRedisSerializer serializer = TedisCacheManager.STRING_SERIALIZER;
    template.setKeySerializer(serializer);
    template.setHashKeySerializer(serializer);
    GenericFastJsonRedisSerializer fastSerializer = TedisCacheManager.FASTJSON_SERIALIZER;
    template.setValueSerializer(fastSerializer);
    template.setHashValueSerializer(fastSerializer);
    template.setDefaultSerializer(fastSerializer);
    template.setConnectionFactory(redisConnectionFactory);
    template.afterPropertiesSet();
    return template;
  }

  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return (o, method, objects) -> {
      StringBuffer sb = new StringBuffer(32);
      sb.append(o.getClass().getSimpleName());
      sb.append(".");
      sb.append(method.getName());
      if (objects.length > 0) {
        sb.append("#");
      }
      String sp = "";
      for (Object object : objects) {
        sb.append(sp);
        if (object == null) {
          sb.append("NULL");
        } else {
          sb.append(object.toString());
        }
        sp = ".";
      }
      return sb.toString();
    };
  }

  /** 配置 RedisCacheManager，使用 cache 注解管理 redis 缓存 */
  @Bean
  @Override
  public CacheManager cacheManager() {
    RedisCacheWriter cacheWriter =
        RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

    RedisCacheConfiguration defaultCacheConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues()
            .serializeKeysWith(TedisCacheManager.STRING_PAIR)
            .serializeValuesWith(TedisCacheManager.FASTJSON_PAIR);
    return new TedisCacheManager(cacheWriter, defaultCacheConfig);
  }
}
