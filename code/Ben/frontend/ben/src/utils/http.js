import axios from "axios";
import { REQUEST } from "../common/view/Constant";
import msg from "./message";

axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded";

const instance = axios.create({ timeout: 10000, crossDomain: true });

instance.interceptors.response.use(
  response => {
    if (response.status === REQUEST.SUCCESS.code) {
      if (response.data.code === REQUEST.SUCCESS.code) {
        return Promise.resolve(response);
      } else if (response.data.code === 301) {
        window.location.replace(process.env.VUE_APP_CENTER);
        return Promise.resolve(response);
      } else {
        msg.error(`${response.data.code}: ${response.data.msg}`);
        return Promise.reject(response);
      }
    } else {
      return Promise.reject(response);
    }
  },
  error => {
    if (error.response) {
      if (error.response.data.msg) {
        msg.error(`${error.response.status}: ${error.response.data.msg}`);
      } else {
        msg.error(`${error.response.status}: ${error.response.statusText}`);
      }
      return Promise.reject(error.response);
    } else {
      msg.error(REQUEST.ERROR.code, REQUEST.ERROR.msg);
      msg.error(REQUEST.ERROR.msg, "3秒后自动刷新");
      setTimeout(() => {
        window.location.reload();
      }, 3000);
      return Promise.reject(error);
    }
  }
);

export default instance;
