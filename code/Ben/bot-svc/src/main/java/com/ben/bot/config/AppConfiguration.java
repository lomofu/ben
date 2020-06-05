package com.ben.bot.config;

import com.ben.common.aspect.ErrorLogAspect;
import com.ben.common.config.RedisConfig;
import com.ben.common.utils.RedisHelper;
import io.goeasy.GoEasy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Resource;

@Slf4j
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
@Import(value = {RedisConfig.class, RedisHelper.class, ErrorLogAspect.class})
public class AppConfiguration {

  @Resource private EndpointConfiguration endpointConfiguration;

  @Resource private ConfigurableEnvironment environment;

  @Resource private GoEasyConfiguration easyConfiguration;

  private String env;

  @Bean
  public ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
    return new ReactiveStringRedisTemplate(factory);
  }

  @Bean
  public GoEasy goEasy() {
    return new GoEasy(easyConfiguration.getHost(), easyConfiguration.getKey());
  }

  public void printfInfo() {
    log.info("======================================");
    log.info("   环境: {}", getEnv());
    log.info("   系统: {}", System.getProperty("os.name"));
    log.info("   服务: {}", environment.getProperty("spring.application.name"));
    log.info("======================================");
  }
}
