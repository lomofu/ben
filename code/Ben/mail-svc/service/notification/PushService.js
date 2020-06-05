const SMTP = require("../../config/SMTP");
const MailService = require("../mail/MailService");
const pushMessageTemplate = require("../../template/notification/pushMessageTemplate");
const pushScheduleTemplate = require("../../template/schedule/pushScheduleTemplate");

class PushService {
  static async sendEmailWithPushNotification(to, title, content) {
    await PushService.mailService.send(pushMessageTemplate(to, title, content));
  }

  static async sendEmailWithPushSchedule(to, list) {
    let array = [];
    list.forEach(e => {
      array.push(`<p>${e.begin} - ${e.end} ${e.name}</p>`);
    });
    let str = array.join(" ");
    await PushService.mailService.send(pushScheduleTemplate(to, str));
  }
}

PushService.mailService = new MailService(SMTP.server, SMTP.user, SMTP.pass);

module.exports = PushService;
