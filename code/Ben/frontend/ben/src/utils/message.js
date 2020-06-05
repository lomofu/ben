import { message } from "ant-design-vue";

message.config({
  duration: 4,
  maxCount: 3
});

const msg = {
  success(data) {
    message.success(data);
  },
  error(data) {
    message.error(data);
  },
  warn(data) {
    message.warning(data);
  }
};
export default msg;
