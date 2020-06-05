package com.ben.company.dao;

import com.ben.company.domain.Job;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface JobMapper extends Mapper<Job> {

  @Select(
      value =
          "SELECT j.id,j.name,j.description,j.start,j.end,j.color,j.full,j.project_id as projectId,j.publish,j.active,j.account_id as accountId "
              + "FROM job j "
              + "WHERE j.start BETWEEN #{start,jdbcType=VARCHAR} AND #{end,jdbcType=VARCHAR} "
              + "AND j.project_id = #{projectId,jdbcType=VARCHAR} AND j.active = 1 AND publish = 0")
  List<Job> selectJobsRangeProject(
      @Param("start") String start, @Param("end") String end, @Param("projectId") String projectId);

  @Select(
      value =
          "SELECT j.id,j.name,j.description,j.start,j.end,j.color,j.full,j.project_id as projectId,j.publish,j.active,j.account_id as accountId "
              + "FROM job j "
              + "WHERE j.start BETWEEN #{start,jdbcType=VARCHAR} AND #{end,jdbcType=VARCHAR} "
              + "AND j.account_id = #{accountId,jdbcType=VARCHAR} AND j.active = 1")
  List<Job> selectJobsRangeAccount(
      @Param("start") String start, @Param("end") String end, @Param("accountId") String accountId);

  // 查询 输入的 start 和 end 是否在数据库中存在 开始时间在这个区间之间
  @Select(
      value =
          "SELECT * FROM job j "
              + "WHERE j.start BETWEEN #{start,jdbcType=VARCHAR} AND #{end,jdbcType=VARCHAR} "
              + "AND j.account_id = #{accountId,jdbcType=VARCHAR} AND j.active = 1  AND j.full = 0")
  List<Job> selectExistJobBetweenBeginAndEndForAccount(
      @Param("start") String start, @Param("end") String end, @Param("accountId") String accountId);

  // 查询 输入的 start 和 end 是否在数据库中存在 结束时间在这个区间之间
  @Select(
      value =
          "SELECT * FROM job j "
              + "WHERE j.end BETWEEN #{start,jdbcType=VARCHAR} AND #{end,jdbcType=VARCHAR} "
              + "AND j.account_id = #{accountId,jdbcType=VARCHAR} AND j.active = 1  AND j.full = 0")
  List<Job> selectExistJobBetweenBeginAndEndForAccount1(
      @Param("start") String start, @Param("end") String end, @Param("accountId") String accountId);

  // 查询 输入的 start 和 end 是否在数据库中存在的 开始时间小于start 结束大于end (外包)
  @Select(
      value =
          "SELECT * FROM job j "
              + "WHERE j.start <= #{start,jdbcType=VARCHAR} AND j.end >= #{end,jdbcType=VARCHAR} "
              + "AND j.account_id = #{accountId,jdbcType=VARCHAR} AND j.active = 1  AND j.full = 0")
  List<Job> selectExistJobBetweenBeginAndEndForAccount2(
      @Param("start") String start, @Param("end") String end, @Param("accountId") String accountId);

  // 查询 输入的 start 和 end 是否在数据库中存在的 开始时间大于start 结束小于end (包含)
  @Select(
      value =
          "SELECT * FROM job j "
              + "WHERE j.start >= #{start,jdbcType=VARCHAR} AND j.end <= #{end,jdbcType=VARCHAR} "
              + "AND j.account_id = #{accountId,jdbcType=VARCHAR} AND j.active = 1  AND j.full = 0")
  List<Job> selectExistJobBetweenBeginAndEndForAccount3(
      @Param("start") String start, @Param("end") String end, @Param("accountId") String accountId);
}
