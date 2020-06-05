package com.ben.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ben.common.exception.CustomException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ben.common.constant.AuthConstant.*;
import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;
import static com.ben.common.constant.SecretConstant.DEFAULT_SECRET_TOKEN;
import static io.undertow.server.protocol.ajp.AjpRequestParser.SECRET;

/**
 * @author lomofu
 * @date 2020/2/15 23:34
 */
public final class JwtHelper {

  protected static final Map<String, Object> header = new HashMap<>();

  static {
    header.put("alg", "HS256");
    header.put("typ", "JWT");
  }

  private JwtHelper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  /**
   * 生成私有密钥
   *
   * @return
   */
  public static String generalKey() {
    return Base64Helper.decodeText(DEFAULT_SECRET_TOKEN);
  }

  /**
   * 生成token
   *
   * @param id 用户id
   * @param name 用户名
   * @param role 用户角色
   * @param expiresDate 过期时间
   * @return
   */
  public static String createToken(String id, String name, String role, Date expiresDate) {
    return JWT.create()
        .withHeader(header)
        .withClaim(CLAIM_ID, id)
        .withClaim(CLAIM_NAME, name)
        .withClaim(CLAIM_ROLE, role)
        .withExpiresAt(expiresDate)
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC256(generalKey()));
  }

  /**
   * 生成临时token用于注册验证
   *
   * @param email 邮箱
   * @param expiresDate 过期时间
   * @return
   */
  public static String createTempToken(String email, Date expiresDate) {
    return JWT.create()
        .withHeader(header)
        .withClaim(CLAIM_EMAIL, email)
        .withExpiresAt(expiresDate)
        .withIssuedAt(expiresDate)
        .sign(Algorithm.HMAC256(SECRET));
  }

  public static String createGateWayToken(Map<String, Claim> claimMap) {
    return getString(claimMap);
  }
  /**
   * 校验token
   *
   * @param token
   * @return
   */
  public static Map<String, Claim> verifyToken(String token) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(generalKey())).build();
    DecodedJWT jwt;
    try {
      jwt = verifier.verify(token);
    } catch (Exception e) {
      throw new CustomException("凭证过期！");
    }
    return jwt.getClaims();
  }

  protected static String getString(Map<String, Claim> claimMap) {
    return JWT.create()
        .withHeader(header)
        .withClaim(CLAIM_ID, claimMap.get(CLAIM_ID).asString())
        .withClaim(CLAIM_NAME, claimMap.get(CLAIM_NAME).asString())
        .withClaim(CLAIM_ROLE, claimMap.get(CLAIM_ROLE).asString())
        .withClaim(CLAIM_CHECK, true)
        .withExpiresAt(TimeHelper.addDay(new Date(), 7))
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC256(generalKey()));
  }
}
