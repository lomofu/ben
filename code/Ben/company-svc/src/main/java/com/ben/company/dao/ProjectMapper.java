package com.ben.company.dao;

import com.ben.company.domain.Project;
import com.ben.company.domain.ProjectList;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProjectMapper extends Mapper<Project> {

  Integer selectCountProjectByCompanyId(@Param("companyId") String companyId);

  List<ProjectList> selectProjectListByCompanyId(@Param("companyId") String companyId);

  List<ProjectList> selectProjectListByAccountId(@Param("accountId") String accountId);
}
