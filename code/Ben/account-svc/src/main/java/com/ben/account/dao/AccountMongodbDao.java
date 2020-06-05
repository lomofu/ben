package com.ben.account.dao;

import com.ben.account.domain.MongoAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMongodbDao extends MongoRepository<MongoAccount, String> {

  MongoAccount findByEmail(String email);

  List<MongoAccount> findMongoAccountByEmail(String email);

  List<MongoAccount> findByPhoneNumber(String phoneNumber);

  List<MongoAccount> findByName(String name);

  MongoAccount findByPhoneNumberOrEmailOrName(String phoneNumber, String email, String name);
}
