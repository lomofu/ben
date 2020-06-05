package com.ben.account.validator;

import com.ben.account.config.AppConfiguration;
import com.ben.account.dao.AccountDao;
import com.ben.account.dao.AccountMongodbDao;
import com.ben.account.dao.EmployeeMongodbDao;
import com.ben.account.dao.MailMongodbDao;
import com.ben.account.enums.AccountEnum;
import com.ben.account.exception.AccountException;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author lomofu
 * @date 2020/2/5 22:17
 */
public enum ValidatorEnum implements ValidatorOperation {
  EMAIL_VALIDATOR {
    @Override
    public boolean op(
            AccountDao accountDao,
            AccountMongodbDao accountMongodbDao,
            MailMongodbDao mailMongodbDao,
            EmployeeMongodbDao employeeMongodbDao,
            String str,
            AppConfiguration appConfiguration) {

      CompletableFuture<Boolean> mysqlEmail =
          CompletableFuture.supplyAsync(
              () -> Optional.ofNullable(accountDao.findByEmailAndActiveTrue(str)).isPresent(),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> accountMongoDBEmail =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(accountMongodbDao.findMongoAccountByEmail(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> mailMongoDBEmail =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(mailMongodbDao.findByEmail(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> employeeMongoDBEmail =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(employeeMongodbDao.findByEmail(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());

      return mysqlEmail
          .thenCombine(accountMongoDBEmail, (i, j) -> i || j)
          .thenCombine(mailMongoDBEmail, (i, j) -> i || j)
          .thenCombine(employeeMongoDBEmail, (i, j) -> i || j)
          .join();
    }
  },
  NAME_VALIDATOR {
    @Override
    public boolean op(
            AccountDao accountDao,
            AccountMongodbDao accountMongodbDao,
            MailMongodbDao mailMongodbDao,
            EmployeeMongodbDao employeeMongodbDao,
            String str,
            AppConfiguration appConfiguration) {

      CompletableFuture<Boolean> mysqlAccount =
          CompletableFuture.supplyAsync(
              () -> Optional.ofNullable(accountDao.findByNameAndActiveTrue(str)).isPresent(),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> accountMongoDBName =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(accountMongodbDao.findByName(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> employeeMongoDBName =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(employeeMongodbDao.findByName(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());

      return mysqlAccount
          .thenCombine(accountMongoDBName, (i, j) -> i || j)
          .thenCombine(employeeMongoDBName, (i, j) -> i || j)
          .join();
    }
  },
  PHONE_NUMBER_VALIDATOR {
    @Override
    public boolean op(
            AccountDao accountDao,
            AccountMongodbDao accountMongodbDao,
            MailMongodbDao mailMongodbDao,
            EmployeeMongodbDao employeeMongodbDao,
            String str,
            AppConfiguration appConfiguration) {

      CompletableFuture<Boolean> mysqlPhoneNumber =
          CompletableFuture.supplyAsync(
              () -> Optional.ofNullable(accountDao.findByPhoneNumberAndActiveTrue(str)).isPresent(),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> accountMongoDBPhoneNumber =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(accountMongodbDao.findByPhoneNumber(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());
      CompletableFuture<Boolean> mongodbPhoneNumber =
          CompletableFuture.supplyAsync(
              () ->
                  Optional.ofNullable(employeeMongodbDao.findByPhoneNumber(str))
                      .map(e -> e.size() > 1)
                      .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_EXIST)),
              appConfiguration.getAsyncExecutor());

      return mysqlPhoneNumber
          .thenCombine(accountMongoDBPhoneNumber, (i, j) -> i || j)
          .thenCombine(mongodbPhoneNumber, (i, j) -> i || j)
          .join();
    }
  }
}
