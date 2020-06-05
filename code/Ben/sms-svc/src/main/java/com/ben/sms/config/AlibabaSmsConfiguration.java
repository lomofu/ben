package com.ben.sms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author lomofu
 * @date 2020/3/24 20:10
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.sms.alibaba")
public class AlibabaSmsConfiguration {
  private String accessKeyId;
  private String secretAccessKey;
  private String region;
  private String signId;
  private String domain;
  private String version;
  private Map<String, String> template;
}
