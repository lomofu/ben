import http from "@/utils/http";

const JOB_API =
  process.env.VUE_APP_NAME === "LOCAL"
    ? "http://localhost:8088/service/job"
    : "https://api.lomofu.com/service/job";

const job = {
  getEventListByProjectId(projectId) {
    return http.get(`${JOB_API}/jobListByProjectId`, {
      params: {
        id: projectId
      }
    });
  },
  getTotalJobCount(projectId) {
    return http.get(`${JOB_API}/totalJobCount`, {
      params: {
        projectId: projectId
      }
    });
  },
  getThisWeekJobList(pageFilter) {
    return http.get(`${JOB_API}/thisWeekJobListByAccountId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        data: pageFilter.data
      }
    });
  },
  getAllJobList(pageFilter) {
    return http.get(`${JOB_API}/jobListByAccountId`, {
      params: {
        pageNumber: pageFilter.pageNumber,
        pageSize: pageFilter.pageSize,
        data: pageFilter.data
      }
    });
  },
  saveJob(formData) {
    return http.post(`${JOB_API}/job`, formData);
  },
  updateJob(formData) {
    return http.put(`${JOB_API}/job`, formData);
  },
  deleteJob(id) {
    return http.delete(`${JOB_API}/job/${id}`);
  }
};

export default job;
