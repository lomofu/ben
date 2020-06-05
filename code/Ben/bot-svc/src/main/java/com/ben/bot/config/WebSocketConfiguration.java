package com.ben.bot.config;

import com.ben.bot.handler.EchoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lomofu
 * @date 2020/3/23 22:48
 */
@Configuration
public class WebSocketConfiguration {

  @Resource
  @Bean
  public HandlerMapping webSocketMapping(final EchoHandler echoHandler) {
    final Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/ws", echoHandler);
    final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
    mapping.setUrlMap(map);
    return mapping;
  }

  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter();
  }
}
