const dev = {
  env: "DEV",
  port: 3000,
  mongodb_config: {
    connect: "mongodb://mongodb.mongodb:27017/ben"
  },
  mail_config: {
    get expire() {
      //30 min 过期
      return Date.now() + 30 * 60 * 1000;
    }
  },
  api: {
    get accountSvcBaseApi() {
      return "http://account-svc:8080/api/accounts";
    },
    get botSvcBaseApi() {
      return "http://bot-svc:8080/api/bot";
    }
  }
};

module.exports = dev;
