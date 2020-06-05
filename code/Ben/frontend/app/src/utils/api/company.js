import http from "@/utils/http";

const COMPANY_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/company"
    : "https://api.lomofu.com/service/company";

const company = {
  getAccountAndCompanyByToken() {
    return http.get(`${COMPANY_API}/companyAndAccount`);
  },
  getCompany(id) {
    return http.get(`${COMPANY_API}/company`, {
      params: {
        id: id
      }
    });
  },
  updateCompany(data) {
    return http.put(`${COMPANY_API}/company`, data);
  },
  getTotalCountByAccountId(companyId) {
    return http.get(`${COMPANY_API}/totalCount`, {
      params: {
        companyId: companyId
      }
    });
  }
};

export default company;
