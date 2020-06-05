package com.ben.company.vo.project;

import com.ben.company.dto.project.LeftNavProjectDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/27 23:02
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeftNavProjectVO {
  @Builder.Default private String name = "项目";
  private List<LeftNavProjectDto> children;
}
