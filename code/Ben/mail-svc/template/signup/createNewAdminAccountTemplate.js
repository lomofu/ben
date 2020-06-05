const SMTP = require('../../config/SMTP');
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

/**
 * 创建新用户(Admin) 模版
 *
 * @param to 收件人邮箱
 * @param token 生成的token令牌
 * @returns {{subject: string, from: string, html: string, to: *}}
 */
const template = function (to, token) {
    return {
        from: `"Ben."<${SMTP.user}>`,
        to: to,
        subject: '[Ben] 注册Ben账户',
        html: `
                <p>
                   <h1>欢迎加入Ben!</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <br><br>
                    请确保是本人操作并尝试点击以下链接，或者复制链接地址到浏览器,链接30分后过期
                    <br><br>
                    <br><br>                
                    <a href="${ENDPOINT.register.url}?token=${token}">${ENDPOINT.register.url}?token=${token}</a>
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
