package com.ben.account.service;

import com.ben.account.vo.AccountResponse;
import com.ben.common.domain.PageFilter;

/**
 * @author lomofu
 * @date 2020/3/12 20:45
 */
public interface SearchService {
  AccountResponse findAccountByCompanyId(PageFilter<String> pageFilter, String search);

  AccountResponse findAccountByTeamId(PageFilter<String> pageFilter, String search);

  AccountResponse findAccountByProjectId(PageFilter<String> pageFilter, String search);
}
