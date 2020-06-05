package com.ben.account.dao;

import com.ben.account.domain.EsAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lomofu
 * @date 2020/3/12 20:24
 */
@Repository
public interface AccountEsDao extends ElasticsearchRepository<EsAccount, String> {}
