package com.ben.common.utils;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * @author lomofu
 * @date 2020-01-22 01:20
 */
public class UUIDHelper implements GenId<String> {

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  public String genId(String table, String column) {
    return getUUID();
  }
}
