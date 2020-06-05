package com.ben.company.repository;

import com.ben.company.domain.EsTeam;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lomofu
 * @date 2020/3/13 23:11
 */
public interface TeamEsRepository extends ElasticsearchRepository<EsTeam, String> {}
