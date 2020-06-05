import http from "@/utils/http";

const ACCOUNT_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/account"
    : "https://api.lomofu.com/service/account";

const account = {
  account(email) {
    return http.get(`${ACCOUNT_API}/email`, {
      params: {
        email: email
      }
    });
  },
  getAccount(id) {
    return http.get(`${ACCOUNT_API}/id`, {
      params: {
        id: id
      }
    });
  },
  isUnique(type, value) {
    return http.get(`${ACCOUNT_API}/isUnique`, {
      params: {
        type: type,
        data: value
      }
    });
  },
  updateAccount(formData) {
    return http.put(`${ACCOUNT_API}/account`, formData);
  },
  getEmployeeListByCompanyId(pageFilter, active) {
    return http.get(`${ACCOUNT_API}/accountListByCompanyId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data,
        active: active
      }
    });
  },
  getEmployeeListByTeamId(pageFilter, active) {
    return http.get(`${ACCOUNT_API}/accountListByTeamId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data,
        active: active
      }
    });
  },
  getEmployeeListByProjectId(pageFilter) {
    return http.get(`${ACCOUNT_API}/accountListByProjectId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data
      }
    });
  },
  getSimpleAccountListByProjectId(pageFilter) {
    return http.get(`${ACCOUNT_API}/simpleAccountListByProjectId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data
      }
    });
  },
  createTempEmployee(formData) {
    return http.post(`${ACCOUNT_API}/tempEmployee`, formData);
  },
  updateEmployee(formData) {
    return http.put(`${ACCOUNT_API}/employee`, formData);
  },
  deleteEmployee(list) {
    return http.delete(`${ACCOUNT_API}/employeeList`, {
      data: list
    });
  },
  deleteTempEmployee(list) {
    return http.delete(`${ACCOUNT_API}/tempEmployeeList`, {
      data: list
    });
  },
  upload(file) {
    let formData = new FormData();
    formData.append("file", file);
    return http.post(`${ACCOUNT_API}/avatar`, formData, {
      headers: { "Content-Type": "application/x-www-form-urlencoded" }
    });
  }
};

export default account;
