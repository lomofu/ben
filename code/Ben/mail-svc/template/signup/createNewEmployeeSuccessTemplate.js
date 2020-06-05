const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

/**
 * 用于通知管理员雇员已经加入成功模版
 * @param to 管理员邮箱
 * @param name 雇员名称
 * @returns {{subject: string, from: string, html: string, to: *}}
 */
const template = function(to, name) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: to,
    subject: "[Ben] 雇员加入成功",
    html: `
                <p>
                   <h1>雇员加入成功!</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <br><br>
                    雇员: <span style="color: #00B7FF">${name}</span> 已经加入!<br>
                    <br><br>
                    <br><br>                
                    <a href="${ENDPOINT.base.url}">前往Ben查看</a>
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
