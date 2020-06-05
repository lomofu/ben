package com.ben.gateway.config;

import com.ben.common.config.RedisConfig;
import com.ben.gateway.dto.GatewayEntry;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.UUIDHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Objects;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;

/**
 * @author lomofu
 * @date 2020/2/26 16:59
 */
@Slf4j
@Setter
@Getter
@Configuration
@Import(value = {RedisHelper.class, RedisConfig.class})
@ConfigurationProperties(prefix = "app")
public class AppConfiguration implements WebFilter {

  @Resource private ConfigurableEnvironment environment;

  @Resource private RedisHelper redisHelper;

  private String env;

  @Bean
  public ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public GatewayEntry getGateWayId() {
    GatewayEntry entry =
        GatewayEntry.builder().uuid(UUIDHelper.getUUID()).createTime(new Date()).build();
    redisHelper.set("gateway::" + entry.getUuid(), entry, DEFAULT_CACHE_TIME);
    return entry;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
    InetSocketAddress remoteAddress = serverWebExchange.getRequest().getRemoteAddress();
    String clientIp = Objects.requireNonNull(remoteAddress).getAddress().getHostAddress();
    ServerHttpRequest mutatedServerHttpRequest =
        serverWebExchange.getRequest().mutate().header("X-CLIENT-IP", clientIp).build();
    ServerWebExchange mutatedServerWebExchange =
        serverWebExchange.mutate().request(mutatedServerHttpRequest).build();
    return webFilterChain.filter(mutatedServerWebExchange);
  }

  public void printfInfo() {
    log.info("======================================");
    log.info("   环境: {}", getEnv());
    log.info("   系统: {}", System.getProperty("os.name"));
    log.info("   服务: {}", environment.getProperty("spring.application.name"));
    log.info("   uuid: {}", getGateWayId().getUuid());
    log.info("======================================");
  }
}
