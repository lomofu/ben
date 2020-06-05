const http = require("./http");
const config = require(`../config/env/${process.env.NODE_ENV}`);

const api = {
  accountSvc: {
    isUnique(type, data) {
      return http.get(`${config.api.accountSvcBaseApi}/isUnique`, {
        params: {
          type: type,
          data: data
        }
      });
    },
    getAccount(email) {
      return http.get(`${config.api.accountSvcBaseApi}/account`, {
        params: {
          email: email
        }
      });
    }
  },
  BotSvc: {
    createNewAdmin(data) {
      return http.post(`${config.api.botSvcBaseApi}/mail/createNewAdmin`, data);
    }
  }
};
module.exports = api;
