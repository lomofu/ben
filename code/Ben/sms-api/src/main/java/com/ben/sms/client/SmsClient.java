package com.ben.sms.client;

import com.ben.sms.dto.LoginWithPhoneCodeDto;
import com.ben.sms.vo.SmsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ben.sms.constant.SmsConstant.BEN_SMS_SVC;

/**
 * @author lomofu
 * @date 2020/3/9 00:34
 */
@FeignClient(name = BEN_SMS_SVC, path = "/api/sms", url = "${app.endpoint.sms-service-endpoint}")
public interface SmsClient {
  @PostMapping(path = "/loginWithPhoneCode")
  SmsResponse sendSMSWithLoginWithPhoneCode(
      @RequestBody LoginWithPhoneCodeDto loginWithPhoneCodeDto);
}
