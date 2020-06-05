package com.ben.account.dao;

import com.ben.account.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author lomofu
 * @date 2020/3/15 16:36
 */
@Repository
public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
  Optional<Role> findByNameAndActiveTrue(String name);

  Optional<Role> findByIdAndActiveTrue(String id);

  @Query(
      value =
          "SELECT r.* FROM role r "
              + "LEFT JOIN account_role_mapping m ON r.id = m.role_id "
              + "LEFT JOIN account a ON m.account_id = a.id "
              + "WHERE a.id = :accountId",
      nativeQuery = true)
  List<Role> findRoleByAccountId(String accountId);

  @Query(
      value =
          "SELECT r.* FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1",
      countQuery =
          "SELECT count(1) FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1",
      nativeQuery = true)
  List<Role> findRoleListByCompanyId(String companyId);

  @Query(
      value =
          "SELECT r.* FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1 ORDER BY r.name ASC",
      countQuery =
          "SELECT count(1) FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1",
      nativeQuery = true)
  Page<Role> findRoleListByCompanyIdAsc(String companyId, Pageable pageable);

  @Query(
      value =
          "SELECT r.* FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1 ORDER BY r.name DESC ",
      countQuery =
          "SELECT count(1) FROM role r "
              + "LEFT JOIN ben_company.company_mapping_account cm ON r.create_by = cm.account_id "
              + "WHERE r.create_by= (SELECT id FROM account a WHERE a.name = 'admin' ) OR cm.company_id = :companyId AND r.active = 1",
      nativeQuery = true)
  Page<Role> findRoleListByCompanyIdDesc(String companyId, Pageable pageable);
}
