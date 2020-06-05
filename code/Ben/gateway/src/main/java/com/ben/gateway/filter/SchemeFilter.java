package com.ben.gateway.filter;

import com.ben.gateway.config.AppConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author lomofu
 * @date 2020/3/7 00:36
 */
@Component
public class SchemeFilter implements GlobalFilter, Ordered {

  @Resource private AppConfiguration appConfiguration;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Object uriObj = exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR);
    if (!appConfiguration.getEnv().equalsIgnoreCase("local") && uriObj != null) {
      URI uri = (URI) uriObj;
      uri = this.upgradeConnection(uri, "http");
      exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);
    }
    return chain.filter(exchange);
  }

  private URI upgradeConnection(URI uri, String scheme) {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).scheme(scheme);
    if (uri.getRawQuery() != null) {
      // When building the URI, UriComponentsBuilder verify the allowed characters and does not
      // support the '+' so we replace it for its equivalent '%20'.
      // See issue https://jira.spring.io/browse/SPR-10172
      uriComponentsBuilder.replaceQuery(uri.getRawQuery().replace("+", "%20"));
    }
    return uriComponentsBuilder.build(true).toUri();
  }

  @Override
  public int getOrder() {
    return 10101;
  }
}
