package com.ben.bot;

import com.ben.bot.config.AppConfiguration;
import com.ben.common.utils.SpringContextHelper;
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
public class BotSvcApplication {
  public static void main(String[] args) {
    SpringApplication.run(BotSvcApplication.class, args);
    ApplicationContext applicationContext = SpringContextHelper.getApplicationContext();
    assert applicationContext != null;
    applicationContext.getBean(AppConfiguration.class).printfInfo();
  }
}
