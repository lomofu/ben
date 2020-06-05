const nodeMailer = require("nodemailer");
const Base64 = require("../../utils/Base64Util");

class MailService {
  constructor(service, user, pass) {
    this.service = service;
    this.user = user;
    this.pass = pass;
  }
  async send(template) {
    const pass = Base64.decodeText(this.pass);
    const transportOptions = {
      service: this.service,
      auth: {
        user: this.user,
        pass: pass
      }
    };
    let session = nodeMailer.createTransport(transportOptions);
    await session.sendMail(template);
  }
}

module.exports = MailService;
