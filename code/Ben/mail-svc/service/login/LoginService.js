const SMTP = require("../../config/SMTP");
const MailService = require("../mail/MailService");
const loginToMuchTemplate = require("../../template/login/loginToMuchTemplate");

class LoginService {
  static async sendEmailWithToMuchLogin(email, name) {
    await LoginService.mailService.send(loginToMuchTemplate(email, name));
  }
}
LoginService.mailService = new MailService(SMTP.server, SMTP.user, SMTP.pass);
module.exports = LoginService;
