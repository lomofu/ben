package com.ben.gateway.controller;

import com.ben.gateway.dto.GatewayEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author lomofu
 * @date 2020/3/3 20:10
 */
@RestController
public class GateWayController {

  @Resource private GatewayEntry gatewayEntry;

  @GetMapping("/id")
  public Mono<GatewayEntry> gatewayEntryMono() {
    return Mono.just(gatewayEntry);
  }

  @GetMapping("/health")
  public Mono<String> healthCheck() {
    return Mono.just("ok");
  }
}
