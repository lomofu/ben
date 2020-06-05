package com.ben.account.validator;

import com.ben.account.config.AppConfiguration;
import com.ben.account.dao.AccountDao;
import com.ben.account.dao.AccountMongodbDao;
import com.ben.account.dao.EmployeeMongodbDao;
import com.ben.account.dao.MailMongodbDao;

/**
 * @author lomofu
 * @date 2020/2/5 22:16
 */
public interface ValidatorOperation {
  boolean op(
          AccountDao accountDao,
          AccountMongodbDao accountMongodbDao,
          MailMongodbDao mailMongodbDao,
          EmployeeMongodbDao employeeMongodbDao,
          String str,
          AppConfiguration appConfiguration);
}
