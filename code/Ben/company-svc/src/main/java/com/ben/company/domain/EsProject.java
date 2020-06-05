package com.ben.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lomofu
 * @date 2020/3/13 20:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
@Document(indexName = "project_index", type = "project")
public class EsProject implements Serializable {
  private static final long serialVersionUID = -7187603408407643649L;

  @Id
  @Field(type = FieldType.Keyword, store = true)
  private String id;

  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String name;

  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String description;

  @JsonProperty("team_id")
  @Field(name = "team_id", type = FieldType.Keyword, store = true)
  private String teamId;

  @JsonProperty("team_name")
  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String teamName;

  @JsonProperty("create_time")
  @Field(name = "create_time", type = FieldType.Date, store = true)
  private Date createTime;

  @JsonProperty("update_time")
  @Field(name = "update_time", type = FieldType.Date, store = true)
  private Date updateTime;

  @Field(type = FieldType.Boolean, store = true)
  private boolean active;

  @JsonProperty("company_id")
  @Field(name = "company_id", type = FieldType.Keyword, store = true)
  private String companyId;

  @JsonProperty("company_name")
  @Field(name = "company_name", type = FieldType.Keyword, store = true)
  private String companyName;
}
