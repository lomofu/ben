package com.ben.gateway;

import com.ben.common.utils.SpringContextHelper;
import com.ben.gateway.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableScheduling
@SpringBootApplication
@Import(value = SpringContextHelper.class)
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
    ApplicationContext applicationContext = SpringContextHelper.getApplicationContext();
    assert applicationContext != null;
    applicationContext.getBean(AppConfiguration.class).printfInfo();
  }
}
