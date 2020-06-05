package com.ben.account.service;

import com.ben.account.domain.Account;

/**
 * @author lomofu
 * @date 2020/3/1 06:53
 */
@FunctionalInterface
public interface LoginService {
  Account login();
}
