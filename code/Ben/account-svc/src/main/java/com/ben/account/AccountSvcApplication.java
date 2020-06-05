package com.ben.account;

import com.ben.account.config.AppConfiguration;
import com.ben.common.utils.SpringContextHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import(value = SpringContextHelper.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.ben.company", "com.ben.bot"})
@EnableElasticsearchRepositories(basePackages = "com.ben.account.dao")
public class AccountSvcApplication {

  public static void main(String[] args) {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
    SpringApplication.run(AccountSvcApplication.class, args);
    ApplicationContext applicationContext = SpringContextHelper.getApplicationContext();
    applicationContext.getBean(AppConfiguration.class).printfInfo();
  }
}
