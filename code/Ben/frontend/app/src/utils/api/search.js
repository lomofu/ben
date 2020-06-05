import http from "@/utils/http";

const ACCOUNT_SEARCH_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/search/accounts"
    : "https://api.lomofu.com/service/search/accounts";
const COMPANY_SEARCH_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/search/companies"
    : "https://api.lomofu.com/service/search/companies";

const search = {
  account: {
    searchAccountByCompanyId(pageFilter, searchAccount) {
      return http.get(`${ACCOUNT_SEARCH_API}/account/company`, {
        params: {
          pageNumber: pageFilter.pageNumber,
          pageSize: pageFilter.pageSize,
          sortBy: pageFilter.sortBy,
          sortDesc: pageFilter.sortDesc,
          data: pageFilter.data,
          active: pageFilter.active,
          search: searchAccount
        }
      });
    },
    searchAccountByTeamId(pageFilter, searchAccountByTeam) {
      return http.get(`${ACCOUNT_SEARCH_API}/account/team`, {
        params: {
          pageNumber: pageFilter.pageNumber,
          pageSize: pageFilter.pageSize,
          sortBy: pageFilter.sortBy,
          sortDesc: pageFilter.sortDesc,
          data: pageFilter.data,
          active: pageFilter.active,
          search: searchAccountByTeam
        }
      });
    },
    searchAccountByProjectId(pageFilter, searchAccountByProject) {
      return http.get(`${ACCOUNT_SEARCH_API}/account/project`, {
        params: {
          pageNumber: pageFilter.pageNumber,
          pageSize: pageFilter.pageSize,
          sortBy: pageFilter.sortBy,
          sortDesc: pageFilter.sortDesc,
          data: pageFilter.data,
          active: pageFilter.active,
          search: searchAccountByProject
        }
      });
    }
  },
  company: {
    searchForTeam(pageFilter, searchTeam) {
      return http.get(`${COMPANY_SEARCH_API}/team`, {
        params: {
          pageNumber: pageFilter.pageNumber,
          pageSize: pageFilter.pageSize,
          sortBy: pageFilter.sortBy,
          sortDesc: pageFilter.sortDesc,
          data: pageFilter.data,
          active: pageFilter.active,
          search: searchTeam
        }
      });
    },
    searchForProject(pageFilter, searchProject) {
      return http.get(`${COMPANY_SEARCH_API}/project`, {
        params: {
          pageNumber: pageFilter.pageNumber,
          pageSize: pageFilter.pageSize,
          sortBy: pageFilter.sortBy,
          sortDesc: pageFilter.sortDesc,
          data: pageFilter.data,
          active: pageFilter.active,
          search: searchProject
        }
      });
    },
    getMonthTeamChartData(companyId) {
      return http.get(`${COMPANY_SEARCH_API}/teamMonthLine`, {
        params: {
          companyId: companyId
        }
      });
    },
    getMonthProjectChartData(companyId) {
      return http.get(`${COMPANY_SEARCH_API}/projectMonthLine`, {
        params: {
          companyId: companyId
        }
      });
    },
    getThisWeekJobsChartData(projectId) {
      return http.get(`${COMPANY_SEARCH_API}/thisWeekJobsLine`, {
        params: {
          projectId: projectId
        }
      });
    }
  }
};

export default search;
