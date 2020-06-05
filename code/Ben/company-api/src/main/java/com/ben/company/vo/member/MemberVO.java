package com.ben.company.vo.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/27 23:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberVO {
  @Builder.Default private String name = "团队成员";
  @Builder.Default private String link = "/employees";
  private List<Object> member;
}
