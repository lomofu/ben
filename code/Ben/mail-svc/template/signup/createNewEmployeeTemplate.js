const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

/**
 * 用于通知雇员被添加进公司
 * @param from 管理员名称
 * @param to 雇员邮箱
 * @param companyName 公司名称
 * @param token 生成的token
 * @returns {{subject: string, from: string, html: string, to: *}}
 */
const template = function(from, to, companyName, token) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: to,
    subject: `[Ben] 激活 "${companyName}" 的的员工账户`,
    html: `
                <p>
                   <h1>欢迎使用Ben服务!</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <br><br>
                    您刚刚被 <span style="color:green">${from}</span> 用户，加入到 <span style="color: #00B7FF">${companyName}</span>,<br>
                    请确保是本人是否同意加入,尝试点击以下链接激活账号，或者复制链接地址到浏览器进行操作,链接30分钟过期
                    <br><br>
                    <br><br>                
                    <a href="${ENDPOINT.employee.url}?token=${token}">${ENDPOINT.employee.url}?token=${token}</a>
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
