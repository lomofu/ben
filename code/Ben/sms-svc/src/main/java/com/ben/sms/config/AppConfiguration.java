package com.ben.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.ben.common.aspect.ErrorLogAspect;
import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

@Slf4j
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
@Import(value = {ErrorLogAspect.class})
public class AppConfiguration {

  @Resource private EndpointConfiguration endpointConfiguration;

  @Resource private JDSmsConfiguration jdSmsConfiguration;

  @Resource private AlibabaSmsConfiguration alibabaSmsConfiguration;

  @Resource private ConfigurableEnvironment environment;

  private String env;

  @Bean
  public SmsClient getSmsClient() {
    CredentialsProvider credentialsProvider =
        new StaticCredentialsProvider(
            jdSmsConfiguration.getAccessKeyId(), jdSmsConfiguration.getSecretAccessKey());
    return SmsClient.builder()
        .credentialsProvider(credentialsProvider)
        .httpRequestConfig(new HttpRequestConfig.Builder().protocol(Protocol.HTTP).build())
        .build();
  }

  @Bean
  public IAcsClient client() {
    DefaultProfile profile =
        DefaultProfile.getProfile(
            alibabaSmsConfiguration.getRegion(),
            alibabaSmsConfiguration.getAccessKeyId(),
            alibabaSmsConfiguration.getSecretAccessKey());
    return new DefaultAcsClient(profile);
  }

  public void printfInfo() {
    log.info("======================================");
    log.info("   环境: {}", getEnv());
    log.info("   系统: {}", System.getProperty("os.name"));
    log.info("   服务: {}", environment.getProperty("spring.application.name"));
    log.info("======================================");
  }
}
