package com.ben.account.dao;

import com.ben.account.domain.MongoEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/23 17:30
 */
@Repository
public interface EmployeeMongodbDao extends MongoRepository<MongoEmployee, String> {

  MongoEmployee findByToken(String token);

  List<MongoEmployee> findByEmail(String email);

  List<MongoEmployee> findByPhoneNumber(String phoneNumber);

  List<MongoEmployee> findByName(String name);

  Page<MongoEmployee> findByCompanyId(String companyId, Pageable pageable);

  Page<MongoEmployee> findByTeamId(String teamId, Pageable pageable);

  void deleteMongoEmployeeByEmailAndNameAndPhoneNumber(
      String email, String name, String phoneNumber);
}
