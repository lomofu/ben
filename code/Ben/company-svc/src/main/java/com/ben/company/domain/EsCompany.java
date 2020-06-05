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
@Document(indexName = "company_index", type = "company")
public class EsCompany implements Serializable {
  private static final long serialVersionUID = 4732553021627827881L;

  @Id
  @Field(type = FieldType.Keyword, store = true)
  private String id;

  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String name;

  @Field(type = FieldType.Boolean, store = true)
  private boolean type;

  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String address;

  @Field(
      type = FieldType.Keyword,
      store = true,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_max_word")
  private String description;

  @JsonProperty("account_id")
  @Field(name = "account_id", type = FieldType.Keyword, store = true)
  private String accountId;

  @JsonProperty("create_time")
  @Field(name = "create_time", type = FieldType.Date, store = true)
  private Date createTime;

  @JsonProperty("update_time")
  @Field(name = "update_time", type = FieldType.Date, store = true)
  private Date updateTime;

  @Field(type = FieldType.Boolean, store = true)
  private boolean active;

  @Field(type = FieldType.Long, store = true)
  private Long member;
}
