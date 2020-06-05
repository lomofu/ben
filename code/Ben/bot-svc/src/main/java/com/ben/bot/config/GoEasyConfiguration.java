package com.ben.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lomofu
 * @date 2020/3/23 23:06
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.websocket")
public class GoEasyConfiguration {
  private String host;
  private String key;
}
