const local = {
  env: "LOCAL",
  port: 3000,
  mongodb_config: {
    connect: "mongodb://localhost:27017/ben"
  },
  mail_config: {
    get expire() {
      //5 min 过期
      return Date.now() + 5 * 60 * 1000;
    }
  },
  api: {
    get accountSvcBaseApi() {
      return "http://localhost:8088/service/account";
    },
    get botSvcBaseApi(){
      return "http://localhost:8088/service/bot"
    }
  }
};

module.exports = local;
