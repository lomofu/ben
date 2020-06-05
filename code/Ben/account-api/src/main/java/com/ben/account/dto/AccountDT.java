package com.ben.account.dto;

import com.ben.common.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.ben.common.constant.CommonConstant.ILLEGAL_EMAIL;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

/**
 * @author lomofu
 * @date 2020/3/15 18:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccountDT implements Serializable {
  private static final long serialVersionUID = 365973052083326870L;

  @NotBlank(message = PARAM_MISSING)
  private String id;

  @NotBlank(message = PARAM_MISSING)
  private String name;

  private Integer gender;

  @Email(message = ILLEGAL_EMAIL)
  private String email;

  @PhoneNumber private String phoneNumber;
  private String avatarUrl;

  private Date createTime;

  private boolean isAdmin;

  private boolean isActive;

  private List<String> role;

  private List<String> permissionList;
}
