package com.ben.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lomofu
 * @date 2020/3/10 17:47
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.oss")
public class OssConfiguration {
    private String endpoint;
    private String url;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
}
