import { regex } from "vuelidate/lib/validators/common.js";

export const fullName = {
  illegalChar: function() {
    return regex("illegalChar", /^[a-zA-Z0-9_\u4e00-\u9fa5\s·]+$/);
  },
  underline: function() {
    return regex("underline", /(^_)|(__)|(_+$)/);
  },
  allNumbers: function() {
    return regex("allNumbers", /^\d*\d\d*$/);
  }
};

export const phone = regex("phoneNumber", /^1(3|4|5|7|8)\d{9}$/);

export const code = regex("code", /\d{6}$/);

export const password = regex(
  "pass",
  /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/
);

export const Email = function(val) {
  return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(val);
};
