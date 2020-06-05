import http from "@/utils/http";
const wwwURL =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/www"
    : "https://api.lomofu.com/service/www";
const accountURL =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/account"
    : "https://api.lomofu.com/service/account";

const api = {
  signup: {
    signup(email) {
      return http.post(`${wwwURL}/createNewAdmin`, {
        email: email
      });
    }
  },
  register: {
    validate(token) {
      return http.get(`${wwwURL}/validateToken`, {
        params: {
          token: token
        }
      });
    },
    isUnique(type, value) {
      return http.get(`${accountURL}/isUnique`, {
        params: {
          type: type,
          data: value
        }
      });
    },
    register(formData) {
      return http.post(`${wwwURL}/register`, {
        formData: formData
      });
    }
  },
  login: {
    login(account, password, isRememberMe) {
      return http.post(`${accountURL}/login`, {
        account: account,
        password: password,
        isRememberMe: isRememberMe
      });
    },
    loginWithCode(formData) {
      return http.post(`${accountURL}/loginWithCode`, formData);
    },
    isLogin(token) {
      return http.get(`${accountURL}/login`, {
        params: {
          token: token
        }
      });
    },
    getCode(phoneNumber) {
      return http.get(`${accountURL}/code/${phoneNumber}`);
    }
  },
  employee: {
    getEmployeeInfo(token) {
      return http.get(`${accountURL}/tempEmployee`, {
        params: {
          token: token
        }
      });
    },
    postEmployee(formData) {
      return http.post(`${accountURL}/employee`, formData);
    }
  }
};

export default api;
