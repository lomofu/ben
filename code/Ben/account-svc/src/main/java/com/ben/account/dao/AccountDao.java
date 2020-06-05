package com.ben.account.dao;

import com.ben.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao
    extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {

  Account findByIdAndActiveTrue(String id);

  Account findByEmailAndActiveTrue(String email);

  Account findByNameAndActiveTrue(String name);

  Account findByPhoneNumberAndActiveTrue(String phoneNumber);

  @Modifying(clearAutomatically = true)
  @Query(
      "update Account account "
          + "set account.name=:name, account.gender=:gender, account.email = :email, account.phoneNumber = :phoneNumber, account.active = true "
          + "where account.id = :id")
  int update(
      @Param("name") String name,
      @Param("gender") Integer gender,
      @Param("email") String email,
      @Param("phoneNumber") String phoneNumber,
      @Param("id") String id);

  @Modifying(clearAutomatically = true)
  @Query("update Account account set account.avatarUrl=:avatarUrl where account.id = :id ")
  int update(@Param("avatarUrl") String avatarUrl, @Param("id") String id);

  @Modifying(clearAutomatically = true)
  @Query("update Account account set account.active = false where account.id = :id")
  int deleteEmployeeAccount(@Param("id") String id);

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.* "
              + "FROM ben_account.account a LEFT JOIN ben_company.company_mapping_account m ON a.id = m.account_id "
              + "WHERE m.company_id = :companyId AND a.is_active = 1",
      countQuery =
          "SELECT count(1) "
              + "FROM ben_account.account a LEFT JOIN ben_company.company_mapping_account m ON a.id = m.account_id "
              + "WHERE m.company_id = :companyId AND a.is_active = 1")
  Page<Account> findAccountListByCompanyId(@Param("companyId") String companyId, Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.* "
              + "FROM ben_account.account a LEFT JOIN ben_company.team_mapping_account m ON a.id = m.account_id "
              + "WHERE m.team_id = :teamId AND a.is_active = 1",
      countQuery =
          "SELECT count(1) "
              + "FROM ben_account.account a LEFT JOIN ben_company.team_mapping_account m ON a.id = m.account_id "
              + "WHERE m.team_id = :teamId AND a.is_active = 1")
  Page<Account> findAccountListByTeamId(@Param("teamId") String teamId, Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.* "
              + "FROM ben_account.account a LEFT JOIN ben_company.project_mapping_account m ON a.id = m.account_id "
              + "WHERE m.project_id = :projectId AND a.is_active = 1",
      countQuery =
          "SELECT count(1) "
              + "FROM ben_account.account a LEFT JOIN ben_company.project_mapping_account m ON a.id = m.account_id "
              + "WHERE m.project_id = :projectId AND a.is_active = 1")
  Page<Account> findAccountListByProjectId(@Param("projectId") String projectId, Pageable pageable);
}
