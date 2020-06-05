// import { REQUEST } from "../../common/view/Constant";
// import api from "../../utils/api";
//
const job = {
  state: {
    job: null,
    jobList: null
  },
  getters: {
    getJob({ job }) {
      return job;
    },
    getJobList({ jobList }) {
      return jobList;
    }
  },
  mutations: {
    updateJob(state, job) {
      state.job = job;
    },
    updateJobList(state, list) {
      state.jobList = list;
    }
  }
};
export default job;
