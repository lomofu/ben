package com.ben.account.dao;

import com.ben.account.domain.RolePermMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author lomofu
 * @date 2020/3/15 17:04
 */
@Repository
public interface RolePermMappingDao
    extends JpaRepository<RolePermMapping, String>, JpaSpecificationExecutor<RolePermMapping> {
  void deleteByRoleId(String roleId);
}
