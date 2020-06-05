package com.ben.sms;

import com.ben.common.utils.SpringContextHelper;
import com.ben.sms.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableAsync
@EnableWebFlux
@SpringBootApplication
@Import(value = SpringContextHelper.class)
public class SmsSvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmsSvcApplication.class, args);
    ApplicationContext applicationContext = SpringContextHelper.getApplicationContext();
    assert applicationContext != null;
    applicationContext.getBean(AppConfiguration.class).printfInfo();
  }
}
