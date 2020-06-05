package com.ben.bot.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * @author lomofu
 * @date 2020/3/23 22:46
 */
@Component
public class EchoHandler implements WebSocketHandler {
  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    return webSocketSession.send(
        webSocketSession
            .receive()
            .map(msg -> webSocketSession.textMessage("data:".concat(msg.getPayloadAsText()))));
  }
}
