package com.ben.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lomofu
 * @date 2020/3/2 21:13
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (SpringContextHelper.applicationContext == null) {
      SpringContextHelper.applicationContext = applicationContext;
    }
  }
}
