package com.ben.account.service.impl;

import com.ben.account.dao.AccountDao;
import com.ben.account.domain.Account;
import com.ben.account.exception.AccountException;
import com.ben.account.service.LoginService;
import com.ben.account.vo.AccountResponse;
import com.ben.account.vo.LoginVO;
import com.ben.common.utils.JwtHelper;
import com.ben.common.utils.MD5Helper;
import com.ben.common.utils.TimeHelper;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.ben.account.enums.AccountEnum.*;
import static com.ben.common.constant.AuthConstant.OTHER;
import static com.ben.common.constant.AuthConstant.ROLE_ADMIN;
import static com.ben.common.constant.CommonConstant.*;

/**
 * @author lomofu
 * @date 2020-01-18 15:11
 */
public final class LoginContext {

  private static Pattern patternEmail = Pattern.compile(EMAIL_REGX);

  private static Pattern patternPhone = Pattern.compile(PHONE_REGX);

  private LoginContext() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }

  public static AccountResponse login(AccountDao accountDao, LoginVO loginVO) {
    LoginService loginService;
    // 是否记住我
    Date expireTime = getExpireTime(loginVO.isRememberMe());

    if (patternEmail.matcher(loginVO.getAccount()).find()) {
      loginService =
          () ->
              Optional.ofNullable(accountDao.findByEmailAndActiveTrue(loginVO.getAccount()))
                  .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));
    } else if (patternPhone.matcher(loginVO.getAccount()).find()) {
      loginService =
          () ->
              Optional.ofNullable(accountDao.findByPhoneNumberAndActiveTrue(loginVO.getAccount()))
                  .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));
    } else {
      throw new AccountException(INPUT_ACCOUNT_IS_ILLEGAL);
    }

    return Optional.of(loginService)
        .map(LoginService::login)
        .map(
            e -> {
              if (e.getPassword().equals(MD5Helper.passwordMD5(loginVO.getPassword()))) {
                String role = getRole(e.isAdmin());
                String token = JwtHelper.createToken(e.getId(), e.getName(), role, expireTime);
                return new AccountResponse(LOGIN_SUCCESS, token);
              }
              return null;
            })
        .orElse(null);
  }

  public static AccountResponse loginWithCode(
      AccountDao accountDao, String phoneNumber, boolean isRememberMe) {
    Account account = accountDao.findByPhoneNumberAndActiveTrue(phoneNumber);
    String role = getRole(account.isAdmin());
    Date expireTime = getExpireTime(isRememberMe);
    String token = JwtHelper.createToken(account.getId(), account.getName(), role, expireTime);
    return new AccountResponse(LOGIN_SUCCESS, token);
  }

  protected static String getRole(boolean isAdmin) {
    return isAdmin ? ROLE_ADMIN : OTHER;
  }

  protected static Date getExpireTime(boolean isRememberMe) {
    return isRememberMe ? TimeHelper.addDay(new Date(), 3) : TimeHelper.addDay(new Date(), 7);
  }
}
