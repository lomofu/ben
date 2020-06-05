package com.ben.bot.service.impl;

import com.ben.bot.service.mock.MockBeanFactory;
import com.ben.bot.vo.BotResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BotMailServiceImplTest {
  @Tested private BotMailServiceImpl botMailService;

  @Injectable private RocketMQTemplate rocketMQTemplate;

  @Test
  public void should_return_success_when_send_email_with_create_new_admin() {
    Assertions.assertNotNull(rocketMQTemplate);
    new Expectations() {
      {
        rocketMQTemplate.convertAndSend(anyString, any);
      }
    };
    BotResponse response =
        botMailService.sendEmailWithCreateNewAdmin(MockBeanFactory.mockCreateNewAdminDto());
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getCode(), 200);
  }
}
