const env = process.env.NODE_ENV;

module.exports = Object.freeze({
  base: {
    get url() {
      return "xxxxx";
    }
  },
  register: {
    get url() {
      return "xxxxx/register";
    }
  },
  employee: {
    get url() {
      return "xxxxx/employee";
    }
  },
  reset: {
    get url() {
      return "xxxxx/reset";
    }
  }
});
