package com.ben.sms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author lomofu
 * @date 2020/3/8 20:26
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.sms.jd")
public class JDSmsConfiguration {
  private String accessKeyId;
  private String secretAccessKey;
  private String region;
  private String signId;
  private Map<String, String> template;
}
