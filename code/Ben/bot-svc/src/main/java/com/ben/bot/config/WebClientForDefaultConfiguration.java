package com.ben.bot.config;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_BOT;

/**
 * @author lomofu
 * @date 2020/3/4 22:19
 */
@Component
public class WebClientForDefaultConfiguration implements WebClientCustomizer {
  @Override
  public void customize(WebClient.Builder webClientBuilder) {
    webClientBuilder.defaultHeader(AUTHORIZATION_SERVICE, AUTHORIZATION_SERVICE_BOT);
  }
}
