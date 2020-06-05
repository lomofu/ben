import { REQUEST } from "../../common/view/Constant";
import api from "../../utils/api";

const dashboard = {
  state: {
    totalCount: {
      teamCount: 0,
      projectCount: 0,
      memberCount: 0
    }
  },
  getters: {
    getTotalCount({ totalCount }) {
      return totalCount;
    },
    getTeamCount({ totalCount: { teamCount } }) {
      return teamCount;
    },
    getProjectCount({ totalCount: { projectCount } }) {
      return projectCount;
    },
    getMemberCount({ totalCount: { memberCount } }) {
      return memberCount;
    }
  },
  mutations: {
    updateTotalCount(state, data) {
      Object.assign(state.totalCount, data);
    },
    increaseAll(state) {
      Object.keys(state.totalCount).forEach(e => state.totalCount[e]++);
    },
    increaseTeamCount(state) {
      state.totalCount.teamCount++;
    },
    increaseProjectCount(state) {
      state.totalCount.projectCount++;
    },
    increaseMemberCount(state) {
      state.totalCount.memberCount++;
    },
    decreaseAll(state) {
      Object.keys(state.totalCount).forEach(e => state.totalCount[e]--);
    },
    decreaseTeamCount(state) {
      state.totalCount.teamCount--;
    },
    decreaseProjectCount(state) {
      state.totalCount.projectCount--;
    },
    decreaseMemberCount(state) {
      state.totalCount.memberCount--;
    }
  },
  actions: {
    async fetchTotalCount({ commit }, { companyId }) {
      const {
        data: { code, data }
      } = await api.company.getTotalCountByAccountId(companyId);
      if (code === REQUEST.SUCCESS.code) commit("updateTotalCount", data);
    }
  }
};

export default dashboard;
