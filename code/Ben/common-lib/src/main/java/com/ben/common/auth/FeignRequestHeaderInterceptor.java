package com.ben.common.auth;

import com.ben.common.utils.HttpContextHelper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.http.HttpRequest;
import org.springframework.util.StringUtils;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;

/**
 * @author lomofu
 * @date 2020/2/29 22:08
 */
public class FeignRequestHeaderInterceptor implements RequestInterceptor {
  @Override
  public void apply(RequestTemplate requestTemplate) {
    String header = HttpContextHelper.getAuthHeader();
    String service = HttpContextHelper.getServiceHeader();
    if (StringUtils.hasText(header)) {
      requestTemplate.header(AUTHORIZATION_HEADER, header);
      requestTemplate.header(AUTHORIZATION_SERVICE, service);
    }
  }
}
