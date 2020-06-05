const router = require("koa-router")();
const assert = require("assert");

const SigUpService = require("../service/signup/SignUpService");
const ResetService = require("../service/reset/ResetService");
const LoginService = require("../service/login/LoginService");
const PushService = require("../service/notification/PushService");

const { SUCCESS, BAD } = require("../config/Response");
const { ADMIN_IS_EMPTY } = require("../config/Constant");
const { NAME_IS_EMPTY } = require("../config/Constant");
const { SEND_SIGN_UP_SUCCESS_EMAIL_SUCCESS } = require("../config/Constant");
const { TOKEN_IS_EMPTY } = require("../config/Constant");
const { SEND_SIGN_UP_EMAIL_SUCCESS } = require("../config/Constant");
const { EMAIL_IS_EMPTY } = require("../config/Constant");
const { SEND_RESET_PASSWORD_EMAIL_SUCCESS } = require("../config/Constant");
const { SEND_LOGIN_TO_MUCH_EMAIL_SUCCESS } = require("../config/Constant");
const { TITLE_IS_EMPTY } = require("../config/Constant");
const { CONTENT_IS_EMPTY } = require("../config/Constant");
const { LIST_IS_EMPTY } = require("../config/Constant");
const { SEND_SCHEDULE_SUCCESS } = require("../config/Constant");
const { SEND_NOTIFICATION_SUCCESS } = require("../config/Constant");

router.prefix("/api/mail");

//注册新管理员邮件
router.post("/createNewAdmin", async ctx => {
  const { email, token } = ctx.request.body;
  try {
    assert.ok(email, EMAIL_IS_EMPTY);
    assert.ok(token, TOKEN_IS_EMPTY);
    await SigUpService.sendEmailWithCreateNewAdminAccount(email, token);
    ctx.body = SUCCESS(SEND_SIGN_UP_EMAIL_SUCCESS);
  } catch (error) {
    console.error(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//账户注册成功邮件
router.post("/createNewAdminSuccess", async ctx => {
  const { email, name } = ctx.request.body;
  try {
    assert.ok(email, EMAIL_IS_EMPTY);
    assert.ok(name, NAME_IS_EMPTY);
    await SigUpService.sendEmailWithCreateNewAdminAccountSuccess(email, name);
    ctx.body = SUCCESS(SEND_SIGN_UP_SUCCESS_EMAIL_SUCCESS);
  } catch (error) {
    console.error(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//注册新雇员邮件
router.post("/createNewEmployee", async ctx => {
  const { from, to, companyName, token } = ctx.request.body;

  try {
    assert.ok(from, ADMIN_IS_EMPTY);
    assert.ok(to, EMAIL_IS_EMPTY);
    assert.ok(companyName, NAME_IS_EMPTY);
    assert.ok(token, TOKEN_IS_EMPTY);
    await SigUpService.sendEmailWithCreateNewEmployee(
      from,
      to,
      companyName,
      token
    );
    ctx.body = SUCCESS(SEND_SIGN_UP_SUCCESS_EMAIL_SUCCESS);
  } catch (error) {
    console.error(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//通知管理员新雇员邮件
router.post("/createNewEmployeeSuccess", async ctx => {
  const { email, name } = ctx.request.body;

  try {
    assert.ok(email, ADMIN_IS_EMPTY);
    assert.ok(name, NAME_IS_EMPTY);
    await SigUpService.sendEmailWithCreateNewEmployeeSuccess(email, name);
    ctx.body = SUCCESS(SEND_SIGN_UP_SUCCESS_EMAIL_SUCCESS);
  } catch (error) {
    console.error(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//重置密码
router.post("/reset", async ctx => {
  const { email, token } = ctx.request.body;

  try {
    assert.ok(email, ADMIN_IS_EMPTY);
    assert.ok(token, TOKEN_IS_EMPTY);
    await ResetService.sendEmailWithResetPassword(email, token);
    ctx.body = SUCCESS(SEND_RESET_PASSWORD_EMAIL_SUCCESS);
  } catch (error) {
    console.log(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//账户异常
router.post("/loginToMuch", async ctx => {
  const { email, account } = ctx.request.body;
  try {
    assert.ok(email, EMAIL_IS_EMPTY);
    assert.ok(account, NAME_IS_EMPTY);
    await LoginService.sendEmailWithToMuchLogin(email, account);
    ctx.body = SUCCESS(SEND_LOGIN_TO_MUCH_EMAIL_SUCCESS);
  } catch (error) {
    console.log(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//公告推送
router.post("/pushNotification", async ctx => {
  const { to, title, content } = ctx.request.body;
  try {
    assert.ok(to, EMAIL_IS_EMPTY);
    assert.ok(title, TITLE_IS_EMPTY);
    assert.ok(content, CONTENT_IS_EMPTY);
    await PushService.sendEmailWithPushNotification(to, title, content);
    ctx.body = SUCCESS(SEND_NOTIFICATION_SUCCESS);
  } catch (error) {
    console.log(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

//排班推送
router.post("/pushSchedule", async ctx => {
  const { to, list } = ctx.request.body;
  assert.ok(to, EMAIL_IS_EMPTY);
  assert.ok(list, LIST_IS_EMPTY);
  try {
    await PushService.sendEmailWithPushSchedule(to, list);
    ctx.body = SUCCESS(SEND_SCHEDULE_SUCCESS);
  } catch (error) {
    console.log(error);
    ctx.status = BAD().code;
    ctx.body = BAD();
  }
});

module.exports = router;
