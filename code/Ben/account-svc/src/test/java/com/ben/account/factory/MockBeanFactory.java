package com.ben.account.factory;

import com.ben.account.domain.Role;
import com.ben.account.vo.NewRoleVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/4/14 20:01
 */
public class MockBeanFactory {
  public static Role buildRole() {
    return Role.builder()
        .id("40289f7a70dd5ca70170dd5cbfe60000")
        .name("管理员")
        .description("管理员")
        .createBy("666")
        .active(true)
        .build();
  }

  public static List<Role> buildRoleList() {
    List<Role> list = new ArrayList<>(6);
    list.add(buildRole());
    list.add(
        Role.builder()
            .id("40289f7a70dd5d910170dd5daa4b0000")
            .name("雇员")
            .description("雇员")
            .createBy("666")
            .active(true)
            .build());
    return list;
  }

  public static NewRoleVO buildRoleVO() {
    return NewRoleVO.builder()
        .name("经理")
        .description("经理")
        .createBy("666")
        .companyId("de59b70e6b674ba287ce598f00466c9f")
        .build();
  }
}
