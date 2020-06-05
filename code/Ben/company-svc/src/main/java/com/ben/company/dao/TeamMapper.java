package com.ben.company.dao;

import com.ben.company.domain.Team;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TeamMapper extends Mapper<Team> {

  @Select(
      value =
          "SELECT id, name, description, company_id, create_time AS createTime, update_time AS updateTime, active FROM team WHERE company_id = #{companyId,jdbcType=VARCHAR} AND active = 1")
  List<Team> selectTeamListByCompanyId(@Param("companyId") String companyId);

  @Select(
      value =
          "SELECT t.id AS id,t.name AS name,t.description AS description,t.company_id AS companyId,t.create_time AS createTime,t.update_time AS updateTime, t.active AS active "
              + "FROM team t LEFT JOIN team_mapping_account m ON t.id = m.team_id "
              + "WHERE account_id = #{accountId,jdbcType=VARCHAR} AND active = 1")
  List<Team> selectTeamListByAccountId(@Param("accountId") String accountId);

  @Select(
      value =
          "SELECT t.id AS id,t.name AS name "
              + "FROM team t LEFT JOIN team_mapping_account m ON t.id = m.team_id "
              + "WHERE account_id = #{accountId,jdbcType=VARCHAR} AND active = 1")
  List<Team> selectSimpleTeamListByAccountId(@Param("accountId") String accountId);
}
