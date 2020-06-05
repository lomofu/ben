import http from "@/utils/http";

const PUSH_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/push"
    : "https://api.lomofu.com/service/push";

const push = {
  getMessage(accountId, pageFilter) {
    return http.get(`${PUSH_API}/message/${accountId}`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize
      }
    });
  },
  pushNotification(formData) {
    return http.post(`${PUSH_API}/notification`, formData);
  },
  pushSchedule(formData) {
    return http.post(`${PUSH_API}/schedule`, formData);
  }
};

export default push;
