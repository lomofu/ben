package com.ben.account.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author lomofu
 * @date 2020/3/28 14:25
 */
@Configuration
public class DataSourceConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource druidDataSource() {
    return new DruidDataSource();
  }

  @Primary
  @Bean("dataSource")
  public DataSourceProxy dataSource(DataSource druidDataSource) {
    return new DataSourceProxy(druidDataSource);
  }
}
