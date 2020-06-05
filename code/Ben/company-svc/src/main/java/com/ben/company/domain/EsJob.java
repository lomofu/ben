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
 * @date 2020/4/1 21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
@Document(indexName = "job_index", type = "job")
public class EsJob implements Serializable {
  private static final long serialVersionUID = -7620936801198793302L;

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

  @JsonProperty("project_id")
  @Field(name = "project_id", type = FieldType.Keyword, store = true)
  private String projectId;

  @JsonProperty("account_id")
  @Field(name = "account_id", type = FieldType.Keyword, store = true)
  private String accountId;

  @Field(type = FieldType.Boolean, store = true)
  private boolean publish;

  @Field(type = FieldType.Keyword, store = true)
  private String color;

  @Field(name = "start", type = FieldType.Date, store = true)
  private Date start;

  @Field(name = "end", type = FieldType.Date, store = true)
  private Date end;

  @Field(type = FieldType.Boolean, store = true)
  private boolean full;

  @Field(type = FieldType.Boolean, store = true)
  private boolean active;
}
