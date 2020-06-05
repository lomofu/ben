package com.ben.common.utils;

import com.ben.common.exception.CustomException;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;

public class MD5Helper {

  private MD5Helper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static String hex(byte[] array) {
    StringBuffer sb = new StringBuffer();
    for (byte b : array) {
      sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
    }
    return sb.toString();
  }

  public static String md5Hex(String message) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return hex(md.digest(message.getBytes("CP1252")));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new CustomException(e.getMessage());
    }
  }

  public static String passwordMD5(@NotBlank String password) {
    return DigestUtils.md5DigestAsHex(password.getBytes());
  }
}
