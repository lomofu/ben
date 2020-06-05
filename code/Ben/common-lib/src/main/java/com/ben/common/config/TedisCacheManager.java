package com.ben.common.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.ben.common.annotation.CacheExpire;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author lomofu
 * @date 2020/2/27 21:29
 */
public class TedisCacheManager extends RedisCacheManager
    implements ApplicationContextAware, InitializingBean {
  public static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
  /**
   * value serializer
   *
   * <pre>
   *     使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException
   *     FastJsonRedisSerializer<Object> fastSerializer = new FastJsonRedisSerializer<>(Object.class);
   * </pre>
   */
  public static final GenericFastJsonRedisSerializer FASTJSON_SERIALIZER =
      new GenericFastJsonRedisSerializer();

  public static final RedisSerializationContext.SerializationPair<String> STRING_PAIR =
      RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER);

  public static final RedisSerializationContext.SerializationPair<Object> FASTJSON_PAIR =
      RedisSerializationContext.SerializationPair.fromSerializer(FASTJSON_SERIALIZER);
  private ApplicationContext applicationContext;
  private Map<String, RedisCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

  public TedisCacheManager(
      RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
    super(cacheWriter, defaultCacheConfiguration);
  }

  @Override
  public Cache getCache(String name) {
    Cache cache = super.getCache(name);
    return new RedisCacheWrapper(cache);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() {
    String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
    for (String beanName : beanNames) {
      final Class clazz = applicationContext.getType(beanName);
      add(clazz);
    }
    super.afterPropertiesSet();
  }

  @Override
  protected Collection<RedisCache> loadCaches() {
    List<RedisCache> caches = new LinkedList<>();
    for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
      caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
    }
    return caches;
  }

  private void add(final Class clazz) {
    ReflectionUtils.doWithMethods(
        clazz,
        method -> {
          ReflectionUtils.makeAccessible(method);
          CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
          if (cacheExpire == null) {
            return;
          }
          Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
          if (cacheable != null) {
            add(cacheable.cacheNames(), cacheExpire);
            return;
          }
          Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
          if (caching != null) {
            Cacheable[] cs = caching.cacheable();
            if (cs.length > 0) {
              for (Cacheable c : cs) {
                if (c != null) {
                  add(c.cacheNames(), cacheExpire);
                }
              }
            }
          } else {
            CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
            if (cacheConfig != null) {
              add(cacheConfig.cacheNames(), cacheExpire);
            }
          }
        },
        method -> null != AnnotationUtils.findAnnotation(method, CacheExpire.class));
  }

  private void add(String[] cacheNames, CacheExpire cacheExpire) {
    for (String cacheName : cacheNames) {
      if (cacheName == null || "".equals(cacheName.trim())) {
        continue;
      }
      long expire = cacheExpire.expire();
      if (expire >= 0) {
        RedisCacheConfiguration config =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expire))
                .disableCachingNullValues()
                .serializeKeysWith(STRING_PAIR)
                .serializeValuesWith(FASTJSON_PAIR);
        initialCacheConfiguration.put(cacheName, config);
      }
    }
  }

  protected static class RedisCacheWrapper implements Cache {
    private final Cache cache;

    RedisCacheWrapper(Cache cache) {
      this.cache = cache;
    }

    @Override
    public String getName() {
      try {
        return cache.getName();
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public Object getNativeCache() {
      try {
        return cache.getNativeCache();
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public ValueWrapper get(Object o) {
      try {
        return cache.get(o);
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
      try {
        return cache.get(o, aClass);
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
      try {
        return cache.get(o, callable);
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public void put(Object o, Object o1) {
      cache.put(o, o1);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
      return cache.putIfAbsent(o, o1);
    }

    @Override
    public void evict(Object o) {
      cache.evict(o);
    }

    @Override
    public void clear() {
      cache.clear();
    }
  }
}
