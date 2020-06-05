const env = process.env.NODE_ENV;

module.exports = Object.freeze({
  base: {
    get url() {
      return "http://localhost:8080";
    }
  },
  register: {
    get url() {
      return "http://localhost:8080/register";
    }
  },
  employee: {
    get url() {
      return "http://localhost:8080/employee";
    }
  },
  reset: {
    get url() {
      return "http://localhost:8080/reset";
    }
  }
});
