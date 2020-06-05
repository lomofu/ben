package com.ben.common.async;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextCopyingDecorator implements TaskDecorator {
  @Override
  public Runnable decorate(Runnable runnable) {
    RequestAttributes context = RequestContextHolder.currentRequestAttributes();
    return () -> {
      try {
        RequestContextHolder.setRequestAttributes(context);
        runnable.run();
      } finally {
        RequestContextHolder.resetRequestAttributes();
      }
    };
  }
}
