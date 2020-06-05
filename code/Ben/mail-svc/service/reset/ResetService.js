const SMTP = require("../../config/SMTP");
const MailService = require("../mail/MailService");
const resetPasswordEmailTemplate = require("../../template/reset/resetPasswordEmailTemplate");

class ResetService {
  static async sendEmailWithResetPassword(to, token) {
    await ResetService.mailService.send(resetPasswordEmailTemplate(to, token));
  }
}

ResetService.mailService = new MailService(SMTP.server, SMTP.user, SMTP.pass);

module.exports = ResetService;
