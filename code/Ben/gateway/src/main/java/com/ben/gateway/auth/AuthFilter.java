package com.ben.gateway.auth;

import com.auth0.jwt.interfaces.Claim;
import com.ben.common.response.BaseResponse;
import com.ben.common.utils.JwtHelper;
import com.ben.gateway.config.AppConfiguration;
import com.ben.gateway.dto.GatewayEntry;
import com.ben.gateway.dto.SkipDto;
import com.ben.gateway.enums.AuthEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.ben.common.constant.AuthConstant.*;
import static com.ben.gateway.enums.AuthEnum.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author lomofu
 * @date 2020/2/29 19:52
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties("auth.url")
public class AuthFilter implements GlobalFilter, Ordered {

  @Resource private ObjectMapper objectMapper;

  @Resource private GatewayEntry gatewayEntry;

  @Resource private AppConfiguration appConfiguration;

  private List<SkipDto> skip;

  @SneakyThrows
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpResponse httpResponse = exchange.getResponse();
    String path = exchange.getRequest().getURI().getPath();
    String methodValue = exchange.getRequest().getMethodValue();

    String env = appConfiguration.getEnv();
    if (env.equalsIgnoreCase("local")) {
      return chain.filter(exchange);
    }

    if (Objects.nonNull(exchange.getRequest().getHeaders().get(AUTHORIZATION_SERVICE))) {
      return authError(httpResponse, ILLEGAL_REQUEST);
    }

    ServerHttpRequest request =
        exchange
            .getRequest()
            .mutate()
            .header("from", "gateway")
            .header(AUTHORIZATION_SERVICE, gatewayEntry.getUuid())
            .build();
    ServerWebExchange build = exchange.mutate().request(request).build();
    // 跳过不需要拦截的请求
    boolean pass =
        Optional.ofNullable(skip)
            .map(List::stream)
            .flatMap(
                e ->
                    e.filter(
                            v ->
                                methodValue.equalsIgnoreCase(v.getMethod())
                                    && path.contains(v.getUrl()))
                        .findAny())
            .isPresent();
    if (pass) {
      return chain.filter(build);
    }
    String token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
    log.info("请求的token: {}", token);
    if (StringUtils.isEmpty(token)) {
      log.error("请求的链接是 {}", exchange.getRequest().getPath());
      return authError(httpResponse, UNAUTHORIZED_TO_LOGIN);
    }
    try {
      Map<String, Claim> claimMap = JwtHelper.verifyToken(token);
      String role = claimMap.get(CLAIM_ROLE).asString();
      request =
          request.mutate().header(AUTHORIZATION_HEADER, token).header(CLAIM_ROLE, role).build();
      return chain.filter(build.mutate().request(request).build());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (e.getMessage().contains("认证过期")) {
        return authError(httpResponse, UNAUTHORIZED_EXPIRE);
      } else {
        return authError(httpResponse, UNAUTHORIZED_FAIL);
      }
    }
  }

  @Override
  public int getOrder() {
    return -200;
  }

  /**
   * 认证错误输出
   *
   * @param response 响应对象
   * @param authEnum 错误枚举
   * @return
   */
  private Mono<Void> authError(ServerHttpResponse response, AuthEnum authEnum) {
    log.error("=====================");
    log.error("错误信息: {}", authEnum.getMessage());
    log.error("=====================");
    response.getHeaders().add(HEADER_CONTENT_TYPE, HEADER_DETAILS);
    response.setStatusCode(UNAUTHORIZED);
    String returnStr = "";
    try {
      returnStr =
          objectMapper.writeValueAsString(
              BaseResponse.builder().code(authEnum.getCode()).msg(authEnum.getMessage()).build());
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
    DataBuffer buffer = response.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Flux.just(buffer));
  }
}
