package com.ben.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lomofu
 * @date 2020/2/1 18:25
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
  @Resource private AppConfiguration appConfiguration;

  @Override
  public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
    configurer.setDefaultTimeout(60 * 1000L);
    configurer.registerCallableInterceptors(timeoutCallableProcessingInterceptor());
    configurer.setTaskExecutor(appConfiguration.getAsyncExecutor());
  }

  @Bean
  public TimeoutCallableProcessingInterceptor timeoutCallableProcessingInterceptor() {
    return new TimeoutCallableProcessingInterceptor();
  }
}
