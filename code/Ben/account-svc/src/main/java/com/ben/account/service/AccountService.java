package com.ben.account.service;

import com.ben.account.dto.AccountDto;
import com.ben.account.dto.PermissionDto;
import com.ben.account.vo.*;
import com.ben.common.domain.PageFilter;
import com.ben.common.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {

  void deleteTempEmployee(String id);

  void deleteEmployee(String token, String id);

  AccountDto findJustAccountById(String id);

  List<AccountDto> findByListId(String[] list);

  List<PermissionDto> getPermissionByAccountId(String id);

  AccountResponse findById(String token, String id);

  AccountResponse findByEmail(String token, String email);

  AccountResponse findByPhoneNumber(String token, String phoneNumber);

  AccountResponse findByName(String token, String name);

  AccountResponse findAccountListByCompanyId(
      String token, PageFilter<String> pageFilter, boolean active);

  AccountResponse findAccountListByTeamId(
      String token, PageFilter<String> pageFilter, boolean active);

  AccountResponse findAccountListByProjectId(String token, PageFilter<String> pageFilter);

  AccountResponse findSimpleAccountListByProjectId(String token, PageFilter<String> pageFilter);

  AccountResponse createNewAccount(String email);

  AccountResponse updateAccount(String token, UpdateVO updateVO);

  AccountResponse getTempEmployeeAccount(String token);

  AccountResponse createTempEmployeeAccountAndSendEmail(String token, TempEmployee tempEmployee);

  AccountResponse createNewEmployee(EmployeeVO employeeVO);

  AccountResponse updateEmployee(UpdateVO updateVO);

  AccountResponse deleteEmployeeList(String token, List<String> employeeListId);

  AccountResponse deleteTempEmployeeList(List<String> tempEmployeeListId);

  AccountResponse deleteTempEmployeeMappingAccount(List<String> list);

  AccountResponse login(LoginVO loginVO);

  AccountResponse loginWithCode(LoginWithCodeVO loginWithCodeVO);

  AccountResponse getLoginCode(String phoneNumber);

  BaseResponse isLogin(String token);

  // AccountResponse resetPassword(String token, String password);

  AccountResponse updateAvatar(String token, UpdateVO updateVO, MultipartFile multipartFile);

  AccountResponse updateAccountRole(String token, UpdateAccountRoleVO updateAccountRoleVO);

  AccountResponse isUnique(String type, String data);

  AccountResponse test();
}
