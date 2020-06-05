const assert = require("assert");
const jwt = require("../../utils/TokenUtil");
const allow = "bot-svc";

const { UNAUTHORIZED } = require("../../public/Response");
/**
 * 检查token合法性
 * @param ctx 请求上下文
 * @returns {Promise<void>}
 */
module.exports.checkToken = async ctx => {
  try {
    const token = ctx.header.authorization;
    assert.ok(token, "token不能为空");
    jwt.parseToken(token);
  } catch (e) {
    console.error(e);
    ctx.status = UNAUTHORIZED.code;
    ctx.body = UNAUTHORIZED;
  }
};

/**
 * 检查是否 来自bot-svc
 * @param ctx 请求上下文
 * @returns {Promise<void>}
 */
module.exports.checkService = async ctx => {
  try {
    const service = ctx.header.service;
    assert.ok(service, "非法请求");
    assert.equal(allow, service, "非法请求");
  } catch (e) {
    console.error(e);
    ctx.status = UNAUTHORIZED.code;
    ctx.body = UNAUTHORIZED;
  }
};
