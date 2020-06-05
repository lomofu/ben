package com.ben.company;

import com.ben.common.utils.SpringContextHelper;
import com.ben.company.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import(value = SpringContextHelper.class)
@MapperScan(basePackages = "com.ben.company.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.ben.account", "com.ben.bot"})
@EnableElasticsearchRepositories(basePackages = "com.ben.company.repository")
public class CompanySvcApplication {
  public static void main(String[] args) {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
    SpringApplication.run(CompanySvcApplication.class, args);
    ApplicationContext applicationContext = SpringContextHelper.getApplicationContext();
    applicationContext.getBean(AppConfiguration.class).printfInfo();
  }
}
