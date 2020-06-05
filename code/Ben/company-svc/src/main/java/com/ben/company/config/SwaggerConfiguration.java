package com.ben.company.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lomofu
 * @date 2020/3/26 16:13
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.ben.company.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiEndPointsInfo())
        .useDefaultResponseMessages(false);
  }

  private ApiInfo apiEndPointsInfo() {
    return new ApiInfoBuilder()
        .title("Company REST API")
        .description("Ben Company服务 REST API")
        .contact(new Contact("lomofu", "https://www.lomofu.com", "lomofu@foxmail.com"))
        .version("v16")
        .build();
  }
}
