import http from "@/utils/http";

const TEAM_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/team"
    : "https://api.lomofu.com/service/team";

const team = {
  getLeftNav(data) {
    return http.get(`${TEAM_API}/leftNav`, {
      params: {
        accountId: data
      }
    });
  },
  createNewTeam(data) {
    return http.post(`${TEAM_API}/team`, data);
  },
  updateTeam(data) {
    return http.put(`${TEAM_API}/team`, data);
  },
  deleteTeam(teamId) {
    return http.delete(`${TEAM_API}/team/${teamId}`);
  },
  getTeamListByCompanyId(pageFilter) {
    return http.get(`${TEAM_API}/teamList`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data
      }
    });
  },
  getSimpleTeamListByAccountId(accountId) {
    return http.get(`${TEAM_API}/teamAllList`, {
      params: {
        accountId: accountId
      }
    });
  },
  deleteTeamListByTeamId(list) {
    return http.delete(`${TEAM_API}/teamList`, { data: list });
  },
  insertAccountListToTeamMapping(formData) {
    return http.post(`${TEAM_API}/teamMapping`, formData);
  },
  removeAccountListToTeamMapping(teamId, active, list) {
    return http.delete(`${TEAM_API}/teamMapping/${teamId}`, {
      params: {
        active: active
      },
      data: list
    });
  }
};

export default team;
