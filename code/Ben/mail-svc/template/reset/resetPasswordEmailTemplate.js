const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

/**
 * 重置密码模版
 * @param to 重置密码账户邮箱
 * @param token 生成token
 * @returns {{subject: string, from: string, html: string, to: *}}
 */
const template = function(to, token) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: to,
    subject: "[Ben] 重置Ben账户密码",
    html: `
                <p>
                    <h1>尊敬的Ben用户您好:</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                     <br>
                    <span>您刚刚在我站进行: <strong style="color: red">重置账户密码操作</strong></span>
                    <br><br>
                    请确保是本人操作并点击以下按钮完成操作
                    <br><br>
                    <button style="height: 40px;width: 80px;padding: 5px;border-radius: 10px;margin-left: 20%">
                        <a style="text-decoration: none" href="${ENDPOINT.reset.url}?token=${token}">重置密码</a>
                    </button>
                    <br><br>
                    如果非本人操作,您的账户可能已经泄漏,请立即进行密码重置,或者联系客服进行账户冻结操作,谢谢！
                    <br><br>
                    如果按钮不生效请尝试点击以下链接，或者复制链接地址到浏览器
                    <br>
                    <a href="${ENDPOINT.reset.url}?token=${token}">${ENDPOINT.reset.url}?token=${token}</a>
                     <br>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <div style="display: flex;justify-content: center">
                      <img src="${SMTP.logo}" style="height: 50px;width: 50px">
                        <h2 style="margin-left: 10px">Ben.</h2>
                    </div>
                </div>`
  };
};

module.exports = template;
