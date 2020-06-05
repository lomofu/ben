const api = require("../../utils/api");
const assert = require("assert");
const { EMAIL } = require("../../public/Constant");
const mailInterface = require("../../interface/mail");
const employeeInterface = require("../../interface/employee");
const accountInterface = require("../../interface/account");
const jwt = require("../../utils/TokenUtil");
const { SUCCESS } = require("../../public/Response");
const config = require(`../../config/env/${process.env.NODE_ENV}`);

class SignUpService {
  static async checkEmailUnique(email) {
    assert(email, EMAIL.EMPTY_EMAIL);
    const { data } = await api.accountSvc.isUnique("email", email);
    const res = await mailInterface.exists({ email: email });
    const res1 = await accountInterface.exists({ email: email });
    const res2 = await employeeInterface.exists({ email: email });
    console.log(res || res1 || res2 || !data.data);
    return res || res1 || res2 || !data.data;
  }

  static async checkAccountUnique(formData) {
    //account账户是否已经存在
    const emailRes = await accountInterface.exists({ email: formData.email });
    const fullNameRes = await accountInterface.exists({ name: formData.name });
    const phoneRes = await accountInterface.exists({
      phoneNumber: formData.phoneNumber
    });
    //employee账户是否存在
    const emailRes1 = await employeeInterface.exists({ email: formData.email });
    const fullNameRes1 = await employeeInterface.exists({
      name: formData.name
    });
    const phoneRes1 = await employeeInterface.exists({
      phoneNumber: formData.phoneNumber
    });
    return (
      emailRes ||
      fullNameRes ||
      phoneRes ||
      emailRes1 ||
      fullNameRes1 ||
      phoneRes1
    );
  }

  static async createNewAdmin(email) {
    const token = jwt.createToken(email);
    let newUser = new mailInterface({
      email: email,
      token: token
    });
    newUser.expireAt = config.mail_config.expire;
    try {
      await newUser.save(email);
      //发送邮件
      api.BotSvc.createNewAdmin({ email: email, token: token });
      return true;
    } catch (e) {
      return false;
    }
  }

  static async createAdminAccount(formData) {
    const account = new accountInterface({
      email: formData.email,
      name: formData.name,
      gender: formData.gender,
      phoneNumber: formData.phoneNumber,
      password: formData.password,
      type: formData.type,
      companyName: formData.companyName,
      confirmPassword: formData.confirmPassword,
      companyAddress: formData.companyAddress
    });
    await account.save();
    const { status, data } = await api.accountSvc.getAccount(formData.email);
    return status === SUCCESS().code && data.code === SUCCESS().code;
  }

  static rollback(email) {
    mailInterface.findOneAndRemove({ email: email }).exec();
    accountInterface.findOneAndRemove({ email: email }).exec();
  }

  static async validateToken(token) {
    return await mailInterface.findOne({ token: token }).exec();
  }
}

module.exports = SignUpService;
