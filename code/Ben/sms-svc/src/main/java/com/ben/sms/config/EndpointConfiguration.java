package com.ben.sms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lomofu
 * @date 2020/2/16 15:00
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.endpoint")
public class EndpointConfiguration {
  private String gatewayServiceEndpoint;
  private String botServiceEndpoint;
}
