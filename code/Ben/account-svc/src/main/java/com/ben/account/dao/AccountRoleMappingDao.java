package com.ben.account.dao;

import com.ben.account.domain.AccountRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/15 17:03
 */
@Repository
public interface AccountRoleMappingDao
    extends JpaRepository<AccountRoleMapping, String>,
        JpaSpecificationExecutor<AccountRoleMapping> {
  AccountRoleMapping findByAccountId(String accountId);

  List<AccountRoleMapping> findByRoleId(String roleId);
}
