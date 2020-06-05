const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

const template = function(email, account) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: email,
    subject: "[Ben] 账号异常",
    html: `
                <p>
                   <h1 style="color: red"> 账号异常</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <br><br>
                    检测到您的账号用户名: <span style="color: #00B7FF"> ${account}</span>
                    <br>登陆Ben错误次数过多, 导致账户异常!
                    <br>请确保是本人操作, 很有可能您的账户信息已经泄漏, 请立即进行修改密码
                    <br><br>
                    <div style="display: flex;justify-content: center">
                    <a href="${ENDPOINT.base.url}">立即前往</a>      
                    </div>
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
