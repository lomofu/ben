package com.ben.gateway.controller;

import com.ben.gateway.dto.GatewayEntry;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class GateWayControllerTest {
  @Tested private GateWayController gateWayController;

  @Injectable private GatewayEntry gatewayEntry;

  @Test
  public void should_return_success_when_gateway_entry_mono() {
    Assertions.assertNotNull(gatewayEntry);
    Mono<GatewayEntry> mono = gateWayController.gatewayEntryMono();
    Assertions.assertNotNull(mono);
    Assertions.assertEquals(mono.block(), gatewayEntry);
  }
}
