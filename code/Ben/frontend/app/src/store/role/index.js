import api from "../../utils/api";
import store from "../index";
import { REQUEST } from "../../common/view/Constant";
const role = {
  state: {
    role: null,
    roleList: [],
    simpleRoleList: []
  },
  getters: {
    getRole({ job }) {
      return job;
    },
    getRoleLists({ roleList }) {
      return roleList;
    },
    getSimpleRoleList({ simpleRoleList }) {
      return simpleRoleList;
    }
  },
  mutations: {
    updateRole(state, job) {
      state.job = job;
    },
    updateRoleList(state, list) {
      state.roleList = list;
    },
    updateSimpleRoleList(state, list) {
      state.simpleRoleList = list;
    }
  },
  actions: {
    async fetchSimpleRoleList({ commit }) {
      const {
        data: { code, data }
      } = await api.role.getSimpleRoleListByCompanyId(
        store.getters.getCompany.id
      );
      if (REQUEST.SUCCESS.code === code) {
        commit("updateSimpleRoleList", data);
      }
    }
  }
};
export default role;
