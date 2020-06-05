import { REQUEST } from "../../common/view/Constant";
import api from "../../utils/api";

const perm = {
  state: {
    list: [],
    roleList: [],
    setting: []
  },
  getters: {
    getAllPerms({ list }) {
      return list;
    },
    getRoleList({ roleList }) {
      return roleList;
    },
    getSetting({ setting }) {
      return setting;
    }
  },
  mutations: {
    updateAllPerm(state, list) {
      state.list = list;
    },
    updateRoleList(state, list) {
      state.roleList = list;
    },
    updateSetting(state, list) {
      state.setting = list;
    }
  },
  actions: {
    async fetchAllPerms({ commit }) {
      const {
        data: { code, data }
      } = await api.perm.getAllPerms();
      if (REQUEST.SUCCESS.code === code) {
        commit("updateAllPerm", data);
      }
    },
    async fetchSettingMenu({ commit }, roleId) {
      const {
        data: { code, data }
      } = await api.perm.getSettingMenuByRoleId(roleId);
      if (REQUEST.SUCCESS.code === code) {
        commit("updateSetting", data);
      }
    }
  }
};
export default perm;
