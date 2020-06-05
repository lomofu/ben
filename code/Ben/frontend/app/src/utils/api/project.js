import http from "@/utils/http";

const PROJECT_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/project"
    : "https://api.lomofu.com/service/project";

const project = {
  getProjectByProjectId(id) {
    return http.get(`${PROJECT_API}/project`, {
      params: {
        id: id
      }
    });
  },
  createNewProject(data) {
    return http.post(`${PROJECT_API}/project`, data);
  },
  updateProject(data) {
    return http.put(`${PROJECT_API}/project`, data);
  },
  deleteProject(projectId) {
    return http.delete(`${PROJECT_API}/project/${projectId}`);
  },
  getProjectListByAccountId(pageFilter) {
    return http.get(`${PROJECT_API}/projectList`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        sortBy: pageFilter.sortBy,
        sortDesc: pageFilter.sortDesc,
        data: pageFilter.data
      }
    });
  },
  deleteProjectList(list) {
    return http.delete(`${PROJECT_API}/projectList`, {
      data: list
    });
  },
  updateProjectToOtherTeam(data) {
    return http.put(`${PROJECT_API}/projectByTeamId`, data);
  },
  insertAccountListToProjectMapping(formData) {
    return http.post(`${PROJECT_API}/projectMapping`, formData);
  },
  removeAccountListToProjectMapping(id, list) {
    return http.delete(`${PROJECT_API}/projectMapping/${id}`, {
      data: list
    });
  }
};

export default project;
