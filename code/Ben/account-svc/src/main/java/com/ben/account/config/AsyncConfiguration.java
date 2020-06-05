package com.ben.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 异步线程池配置累
 *
 * @author lomofu
 * @date 2020/3/2 20:27
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.async-thread-pool")
public class AsyncConfiguration {
  private int corePoolSize;
  private int maxPoolSize;
  private int queueCapacity;
  private int keepAlive;
  private String threadPrefixName;
  private boolean waitForTasksComplete;
}
