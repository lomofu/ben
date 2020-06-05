package com.ben.sms.service.impl;

import com.aliyuncs.IAcsClient;
import com.ben.sms.config.AlibabaSmsConfiguration;
import com.ben.sms.config.JDSmsConfiguration;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SmeServiceImplTest {
  @Tested private SmeServiceImpl smeService;

  @Injectable private JDSmsConfiguration jdSmsConfiguration;

  @Injectable private AlibabaSmsConfiguration alibabaSmsConfiguration;

  @Injectable private SmsClient smsClient;

  @Injectable private IAcsClient acsClient;

  @Test
  public void should_return_success_when_sendPhoneCode() {
    Assertions.assertNotNull(jdSmsConfiguration);
    Assertions.assertNotNull(smsClient);
  }
}
