const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

const template = function(to, name) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: to,
    subject: "[Ben] 账号注册成功",
    html: `
                <p>
                   <h1>Ben 账号注册成功!</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <br><br>
                    您的账号用户名: <span style="color: #00B7FF">${name}</span> 注册成功!<br>前往以下网址开始使用Ben.吧! (请使用注册填写的邮箱或者手机进行登陆）
                    <br><br>
                    <br><br>                
                    <a href="${ENDPOINT.base.url}">${ENDPOINT.base.url}</a>
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
