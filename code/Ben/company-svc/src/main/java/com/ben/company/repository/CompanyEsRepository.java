package com.ben.company.repository;

import com.ben.company.domain.EsCompany;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lomofu
 * @date 2020/3/13 22:57
 */
public interface CompanyEsRepository extends ElasticsearchRepository<EsCompany, String> {}
