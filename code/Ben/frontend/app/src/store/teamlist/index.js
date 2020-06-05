import { REQUEST } from "../../common/view/Constant";
import api from "../../utils/api";

const teamList = {
  state: {
    simpleTeamList: []
  },
  getters: {
    getSimpleTeamList({ simpleTeamList }) {
      return simpleTeamList;
    }
  },
  mutations: {
    updateSimpleTeamList(state, data) {
      state.simpleTeamList = data;
    }
  },
  actions: {
    async fetchSimpleTeamList({ commit }, accountId) {
      const {
        data: { code, data }
      } = await api.team.getSimpleTeamListByAccountId(accountId);
      if (REQUEST.SUCCESS.code === code) {
        commit("updateSimpleTeamList", data);
      }
    }
  }
};

export default teamList;
