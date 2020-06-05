package com.ben.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;

/**
 * @author lomofu
 * @date 2020/2/29 22:03
 */
public final class HttpContextHelper {

  private static HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
    return null;
  }

  private static String getRequestHeader(String headerName) {
    return Objects.requireNonNull(getRequest()).getHeader(headerName);
  }

  public static String getRequestPath() {
    return Objects.requireNonNull(getRequest()).getRequestURI();
  }

  public static String getParameter() {
    StringBuffer stringBuffer = new StringBuffer();
    HttpServletRequest request = Objects.requireNonNull(getRequest());
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String key = parameterNames.nextElement();
      stringBuffer.append(key).append(" : ").append(request.getParameter(key));
    }
    return stringBuffer.toString();
  }

  public static String getMethod() {
    return Objects.requireNonNull(getRequest()).getMethod();
  }

  public static String getRemoteAddr() {
    return Objects.requireNonNull(getRequest()).getRemoteAddr();
  }

  public static int getRemotePort() {
    return Objects.requireNonNull(getRequest()).getRemotePort();
  }

  public static int getLocalPort() {
    return Objects.requireNonNull(getRequest()).getLocalPort();
  }

  public static String getLocalAddr() {
    return Objects.requireNonNull(getRequest()).getLocalAddr();
  }

  public static String getAuthHeader() {
    return getRequestHeader(AUTHORIZATION_HEADER);
  }

  public static String getServiceHeader() {
    return getRequestHeader(AUTHORIZATION_SERVICE);
  }

  public static String getFromHeader() {
    return getRequestHeader("from");
  }
}
