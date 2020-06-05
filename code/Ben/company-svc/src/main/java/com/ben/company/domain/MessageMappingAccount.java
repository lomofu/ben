package com.ben.company.domain;

import com.ben.common.utils.UUIDHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "message_mapping_account")
public class MessageMappingAccount {
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  @Column(name = "message_id")
  private String messageId;

  @Column(name = "account_id")
  private String accountId;
}
