package com.ben.common.utils;

import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;
import static com.ben.common.constant.SecretConstant.FTP_SECRET;

/**
 * @author lomofu
 * @date 2020-01-22 03:56
 */
public class Base64Helper {

  private static final Base64.Decoder decoder = Base64.getDecoder();
  private static final Base64.Encoder encoder = Base64.getEncoder();

  private Base64Helper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static String encodeText(String origin) {
    return Optional.ofNullable(encoder.encodeToString(origin.getBytes()))
        .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));
  }

  public static String decodeText(String encodedText) {
    return Optional.ofNullable(decoder.decode(encodedText))
        .map(e -> new String(e, StandardCharsets.UTF_8))
        .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));
  }

  public static String getAccount() {
    return decodeText(FTP_SECRET);
  }

  public static String getPassword() {
    return decodeText(FTP_SECRET);
  }
}
