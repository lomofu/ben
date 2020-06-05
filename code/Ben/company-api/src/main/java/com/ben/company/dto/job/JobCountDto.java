package com.ben.company.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/4/2 00:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JobCountDto implements Serializable {
  private static final long serialVersionUID = 4660510077910683313L;
  private int pubCount;
  private int unPubCount;
  private int total;
}
