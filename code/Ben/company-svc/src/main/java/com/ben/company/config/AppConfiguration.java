package com.ben.company.config;

import com.ben.common.aspect.ErrorLogAspect;
import com.ben.common.aspect.ServiceCheckAspect;
import com.ben.common.async.ContextCopyingDecorator;
import com.ben.common.config.RedisConfig;
import com.ben.common.config.RestConfig;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Setter
@Getter
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "app")
@Import(
    value = {
      RestConfig.class,
      RedisConfig.class,
      RedisHelper.class,
      ServiceCheckAspect.class,
      ErrorLogAspect.class,
      RedissonDistributedLocker.class
    })
public class AppConfiguration {
  @Resource private EndpointConfiguration endpointConfiguration;

  @Resource private AsyncConfiguration asyncConfiguration;

  @Resource private ConfigurableEnvironment environment;

  @Resource private RestTemplate restTemplate;

  private String env;

  @Bean
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(asyncConfiguration.getCorePoolSize());
    executor.setMaxPoolSize(asyncConfiguration.getMaxPoolSize());
    executor.setQueueCapacity(asyncConfiguration.getQueueCapacity());
    executor.setWaitForTasksToCompleteOnShutdown(asyncConfiguration.isWaitForTasksComplete());
    executor.setThreadNamePrefix(asyncConfiguration.getThreadPrefixName());
    executor.setKeepAliveSeconds(asyncConfiguration.getKeepAlive());
    // 线程上下文拷贝
    executor.setTaskDecorator(new ContextCopyingDecorator());
    // 使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }

  public void printfInfo() {
    log.info("======================================");
    log.info("   环境: {}", getEnv());
    log.info("   系统: {}", System.getProperty("os.name"));
    log.info("   服务: {}", environment.getProperty("spring.application.name"));
    log.info("======================================");
  }
}
