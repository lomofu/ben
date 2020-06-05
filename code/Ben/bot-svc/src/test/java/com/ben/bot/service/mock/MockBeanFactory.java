package com.ben.bot.service.mock;

import com.ben.bot.dto.mail.CreateNewAdminDto;

/**
 * @author lomofu
 * @date 2020/4/19 16:48
 */
public class MockBeanFactory {
  public static CreateNewAdminDto mockCreateNewAdminDto() {
    return CreateNewAdminDto.builder().email("example@mail.com").token("token").build();
  }
}
