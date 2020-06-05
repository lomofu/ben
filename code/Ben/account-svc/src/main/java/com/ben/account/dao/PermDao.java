package com.ben.account.dao;

import com.ben.account.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/15 17:02
 */
@Repository
public interface PermDao
    extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {

  @Query(
      value =
          "SELECT p.* FROM permission p LEFT JOIN role_perm_mapping m ON p.id = m.perm_id WHERE m.role_id = :roleId",
      nativeQuery = true)
  List<Permission> findPermissionsByRoleId(String roleId);

  @Query(
      value =
          "SELECT p.* FROM permission p LEFT JOIN role_perm_mapping m ON p.id = m.perm_id LEFT JOIN account_role_mapping a ON m.role_id = a.role_id WHERE a.account_id = :accountId",
      nativeQuery = true)
  List<Permission> findPermissionsByAccountId(String accountId);
}
