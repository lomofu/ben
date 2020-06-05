package com.ben.account.dao;

import com.ben.account.domain.MongoReset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lomofu
 * @date 2020-01-21 22:47
 */
@Repository
public interface PasswordMongodbDao extends MongoRepository<MongoReset, String> {
  MongoReset findByToken(String token);
}
