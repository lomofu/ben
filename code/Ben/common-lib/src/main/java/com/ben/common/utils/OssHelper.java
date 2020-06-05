package com.ben.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @author lomofu
 * @date 2020/3/10 17:37
 */
@Slf4j
@Component
public class OssHelper {
  public static final String MY_STYLE = "?x-oss-process=style/mystyle";

  @Resource private OSS ossClient;

  public String upload(String url, String bucket, String filePath, InputStream inputStream) {
    log.info("================ begin to upload avatar =================");
    PutObjectResult putResult = ossClient.putObject(bucket, filePath, inputStream);
    String tag = putResult.getETag();
    ossClient.shutdown();
    log.info("================ success: {} =================", tag);
    return this.getUrl(url, filePath);
  }

  public String getUrl(String url, String filePath) {
    // https://ben-k8s.oss-cn-shanghai.aliyuncs.com/ben/account/ff8080816fecb64a016fecbd23f70001/58afb1be72b3414fb2a18df23d5bf822.png
    return url.concat("/").concat(filePath).concat(MY_STYLE);
  }
}
