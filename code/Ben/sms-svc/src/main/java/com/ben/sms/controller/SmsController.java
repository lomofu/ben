package com.ben.sms.controller;

import com.ben.sms.dto.LoginWithPhoneCodeDto;
import com.ben.sms.vo.SmsResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author lomofu
 * @date 2020/3/9 00:33
 */
@RestController
@RequestMapping("/api/sms")
public class SmsController {

  @PostMapping(path = "/loginWithPhoneCode")
  public Mono<SmsResponse> sendSMSWithLoginWithPhoneCode(
      @RequestBody LoginWithPhoneCodeDto loginWithPhoneCodeDto) {
    return Mono.empty();
  }
}
