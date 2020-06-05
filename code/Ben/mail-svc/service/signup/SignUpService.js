const SMTP = require("../../config/SMTP");
const MailService = require("../mail/MailService");
const createNewAdminAccountTemplate = require("../../template/signup/createNewAdminAccountTemplate");
const createNewAdminAccountSuccessTemplate = require("../../template/signup/createNewAdminAccountSuccessTemplate");
const createNewEmployeeTemplate = require("../../template/signup/createNewEmployeeTemplate");
const createNewEmployeeSuccessTemplate = require("../../template/signup/createNewEmployeeSuccessTemplate");

class SignUpService {
  static async sendEmailWithCreateNewAdminAccount(to, token) {
    await SignUpService.mailService.send(
      createNewAdminAccountTemplate(to, token)
    );
  }

  static async sendEmailWithCreateNewAdminAccountSuccess(to, name) {
    await SignUpService.mailService.send(
      createNewAdminAccountSuccessTemplate(to, name)
    );
  }

  static async sendEmailWithCreateNewEmployee(from, to, companyName, token) {
    await SignUpService.mailService.send(
      createNewEmployeeTemplate(from, to, companyName, token)
    );
  }

  static async sendEmailWithCreateNewEmployeeSuccess(to, name) {
    await SignUpService.mailService.send(
      createNewEmployeeSuccessTemplate(to, name)
    );
  }
}

SignUpService.mailService = new MailService(SMTP.server, SMTP.user, SMTP.pass);

module.exports = SignUpService;
