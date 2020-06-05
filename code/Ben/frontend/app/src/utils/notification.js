import { notification } from "ant-design-vue";

const info = {
  success(msg, des) {
    notification.success({
      message: msg,
      description: des,
      duration: 3
    });
  },
  error(msg, des) {
    notification.error({
      message: msg,
      description: des,
      duration: 3.5
    });
  },
  warn(msg, des) {
    notification.warn({
      message: msg,
      description: des,
      duration: 3.5
    });
  },
  open(key, msg, des) {
    notification.error({
      key,
      message: msg,
      description: des,
      duration: 5
    });
  }
};

export default info;
