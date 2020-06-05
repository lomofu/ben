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
 * @date 2020/3/13 20:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
@Document(indexName = "team_index", type = "team")
public class EsTeam implements Serializable {

  private static final long serialVersionUID = 427001773320060510L;

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

  @JsonProperty("company_id")
  @Field(name = "company_id", type = FieldType.Keyword, store = true)
  private String companyId;

  @JsonProperty("create_time")
  @Field(name = "create_time", type = FieldType.Date, store = true, fielddata = true)
  private Date createTime;

  @JsonProperty("update_time")
  @Field(name = "update_time", type = FieldType.Date, store = true)
  private Date updateTime;

  @Field(type = FieldType.Boolean, store = true)
  private boolean active;

  @Field(type = FieldType.Long, store = true)
  private Long member;
}
