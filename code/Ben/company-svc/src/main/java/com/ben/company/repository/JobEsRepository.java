package com.ben.company.repository;

import com.ben.company.domain.EsJob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lomofu
 * @date 2020/4/1 21:45
 */
public interface JobEsRepository extends ElasticsearchRepository<EsJob, String> {}
