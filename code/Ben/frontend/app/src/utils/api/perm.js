import http from "@/utils/http";

const ACCOUNT_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/account"
    : "https://api.lomofu.com/service/account";

const perm = {
  getPermListByRoleId(roleId) {
    return http.get(`${ACCOUNT_API}/permListByRoleId/${roleId}`);
  },
  getAllPerms() {
    return http.get(`${ACCOUNT_API}/permList`);
  },
  getSettingMenuByRoleId(accountId) {
    return http.get(`${ACCOUNT_API}/setting/${accountId}`);
  }
};

export default perm;
