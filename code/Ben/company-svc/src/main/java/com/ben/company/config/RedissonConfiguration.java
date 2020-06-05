package com.ben.company.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lomofu
 * @date 2020/3/20 13:13
 */
@Configuration
public class RedissonConfiguration {

  @Resource private AppConfiguration appConfiguration;

  @Bean
  public RedissonClient redisson() throws IOException {
    Config config = Config.fromYAML(new ClassPathResource(getResourceYml()).getInputStream());
    return Redisson.create(config);
  }

  protected String getResourceYml() {
    return "redisson-" + appConfiguration.getEnv().toLowerCase() + "-config.yml";
  }
}
