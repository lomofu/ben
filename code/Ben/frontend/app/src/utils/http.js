import axios from "axios";
import { REQUEST } from "../common/view/Constant";
import info from "./notification";
import Vue from "vue";

axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded";

const instance = axios.create({ timeout: 20000, crossDomain: true });

instance.interceptors.request.use(
  config => {
    const token = Vue.$cookies.get("ben-user");
    token && (config.headers.Authorization = token);
    return config;
  },
  error => {
    return Promise.error(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  response => {
    if (response.status === 200) {
      if (response.data.code === 200) {
        return Promise.resolve(response);
      } else {
        info.error(response.data.code, response.data.msg);
        return Promise.reject(response);
      }
    } else {
      return Promise.reject(response);
    }
  },
  error => {
    if (error.response) {
      if (error.response.data.msg) {
        info.error(error.response.status, error.response.data.msg);
      } else {
        info.error(error.response.status, error.response.statusText);
      }
      if (error.response.status === REQUEST.UN_AUTHORIZATION.code) {
        info.error(error.response.status, error.response.data.msg);
        setTimeout(() => {
          window.location.replace(`${process.env.VUE_APP_URL}/login`);
        }, 700);
      }
      return Promise.reject(error.response);
    } else {
      info.error(REQUEST.ERROR.code, REQUEST.ERROR.msg);
      let time = 5;
      const _timer = setInterval(() => {
        if (time > 0) {
          info.open("flush", REQUEST.ERROR.msg, `${time--}秒后自动刷新`);
        } else {
          clearInterval(_timer);
          //window.location.reload();
        }
      }, 1000);
      return Promise.reject(error);
    }
  }
);

export default instance;
