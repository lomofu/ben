module.exports = {
  SUCCESS(msg,data) {
    return {
      code: 200,
      msg: msg === null || msg === undefined ? "邮件发送成功" : msg,
      data:data
    };
  },
  BAD(msg) {
    return {
      code: 400,
      msg: msg === null || msg === undefined ? "邮件发送失败" : msg
    };
  },
  ERROR: {
    code: 500,
    msg: "服务器异常"
  },
  UNAUTHORIZED: {
    code: 401,
    msg: "没有权限"
  }
};
