import http from "@/utils/http";

const ACCOUNT_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/account"
    : "https://api.lomofu.com/service/account";

const role = {
  getRoleList(pageFilter) {
    return http.get(`${ACCOUNT_API}/roleList`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data
      }
    });
  },
  createNewRole(formData) {
    return http.post(`${ACCOUNT_API}/role`, formData);
  },
  updateRole(formData) {
    return http.put(`${ACCOUNT_API}/role`, formData);
  },
  deleteRoleListById(roleIdList) {
    return http.delete(`${ACCOUNT_API}/roleList`, {
      data: roleIdList
    });
  },
  updateRolePerm(roleId, list) {
    return http.put(`${ACCOUNT_API}/rolePerm/${roleId}`, list);
  },
  getSimpleRoleListByCompanyId(companyId) {
    return http.get(`${ACCOUNT_API}/simpleRoleList/${companyId}`);
  },
  updateAccountRole(formData) {
    return http.put(`${ACCOUNT_API}/accountRole`, formData);
  }
};

export default role;
