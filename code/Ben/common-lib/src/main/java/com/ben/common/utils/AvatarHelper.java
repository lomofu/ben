package com.ben.common.utils;

import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;

import java.util.Optional;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;

public final class AvatarHelper {

  public static final String BASE_URL = "https://www.gravatar.com/avatar/%s.jpg?s=150&d=identicon";

  public static final String BEN_ACCOUNT = "ben/account/";

  private AvatarHelper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static String generaDefaultAvatar(String email) {
    String avatarHash = MD5Helper.md5Hex(email);
    return String.format(BASE_URL, avatarHash);
  }

  public static String generaNewAvatarName(String fileName) {
    return UUIDHelper.getUUID() + getSuffix(fileName);
  }

  public static String generaNewOssAvatarNameForAccount(String accountId, String fileName) {
    return BEN_ACCOUNT.concat(accountId).concat("/").concat(generaNewAvatarName(fileName));
  }

  private static String getSuffix(String fileName) {
    return Optional.ofNullable(fileName)
        .map(e -> e.substring(e.lastIndexOf('.')))
        .orElseThrow(() -> new CustomException(ResultCode.INTERNAL_SERVER_ERROR));
  }
}
