const SMTP = require("../../config/SMTP");
const ENDPOINT = require(`../../config/${process.env.NODE_ENV}/EndPoint`);

const template = function(to, title, content) {
  return {
    from: `"Ben."<${SMTP.user}>`,
    to: to,
    subject: "[Ben] 公告通知",
    html: `
                <p>
                   <h1>${title}</h1>
                </p>
                <div>
                    <hr style="border-top: 1px dashed #8c8b8b;">
                    <p>${content}</p>      
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
