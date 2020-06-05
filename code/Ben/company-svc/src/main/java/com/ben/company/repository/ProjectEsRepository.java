package com.ben.company.repository;

import com.ben.company.domain.EsProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lomofu
 * @date 2020/3/13 23:18
 */
public interface ProjectEsRepository extends ElasticsearchRepository<EsProject, String> {}
