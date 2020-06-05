package com.ben.company.dao;

import com.ben.company.domain.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {
  @Select(
      value =
          "SELECT m.id,m.title,m.content,m.create_time as createTime, m.create_by as createBy FROM message_mapping_account a LEFT JOIN message m ON a.message_id = m.id "
              + "WHERE a.account_id = #{accountId,jdbcType=VARCHAR} ORDER BY m.create_time desc")
  List<Message> selectMessagesByAccountId(@Param("accountId") String accountId);
}
