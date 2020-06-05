const router = require("koa-router")();
const assert = require("assert");
const { SUCCESS, BAD } = require("../public/Response");
const { EMAIL, ACCOUNT } = require("../public/Constant");
const SignUpService = require("../service/signup/SignUpService");

router.prefix("/api/signup");

//发送邮件创建新用户
router.post("/createNewAdmin", async ctx => {
  const { email } = ctx.request.body;
  try {
    const res = await SignUpService.checkEmailUnique(email);
    if (res) {
      ctx.status = BAD().code;
      ctx.body = BAD(EMAIL.EMAIL_IS_EXIST);
      return;
    }
  } catch (e) {
    console.log(e);
    ctx.status = BAD().code;
    ctx.body = BAD(e.message);
    return;
  }

  try {
    const res = await SignUpService.createNewAdmin(email);
    if (res) {
      ctx.status = SUCCESS().code;
      ctx.body = SUCCESS(EMAIL.SUCCESS_SEND);
    }
  } catch (e) {
    console.error(e);
    SignUpService.rollback(email);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//校验token合法性
router.get("/validateToken", async ctx => {
  const { token } = ctx.query;
  try {
    assert.ok(token, EMAIL.TOKEN_IS_EMPTY);
  } catch (e) {
    console.error(e);
    ctx.status = BAD().code;
    ctx.body = BAD(EMAIL.TOKEN_IS_EMPTY);
    return;
  }
  const res = await SignUpService.validateToken(token);
  if (res) {
    ctx.status = SUCCESS().code;
    ctx.body = SUCCESS(null, res.email);
  } else {
    ctx.status = BAD().code;
    ctx.body = BAD(EMAIL.EMAIL_IS_EXPIRED);
  }
});

//注册新管理员账户
router.post("/register", async ctx => {
  const { formData } = ctx.request.body;
  try {
    if (await SignUpService.checkAccountUnique(formData)) {
      ctx.status = BAD().code;
      ctx.body = BAD(ACCOUNT.ACCOUNT_IS_EXIST);
    } else {
      if (await SignUpService.createAdminAccount(formData)) {
        ctx.body = SUCCESS();
      } else {
        ctx.status = BAD().code;
        ctx.body = BAD(ACCOUNT.ACCOUNT_REGISTER_FAIL);
      }
    }
  } catch (e) {
    console.error(e);
    ctx.status = BAD().code;
    ctx.body = BAD(ACCOUNT.ACCOUNT_REGISTER_FAIL);
    SignUpService.rollback(formData.email);
  } finally {
    SignUpService.rollback(formData.email);
  }
});

module.exports = router;
