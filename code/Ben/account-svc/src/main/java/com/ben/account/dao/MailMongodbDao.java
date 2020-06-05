package com.ben.account.dao;

import com.ben.account.domain.MongoEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/23 18:39
 */
@Repository
public interface MailMongodbDao extends MongoRepository<MongoEmail, String> {
  List<MongoEmail> findByEmail(String email);
}
