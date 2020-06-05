import { REQUEST } from "../../common/view/Constant";
import api from "../../utils/api";

const left = {
  state: {
    mini: false,
    menu: null
  },
  getters: {
    getMiniIndex({ mini }) {
      return mini;
    },
    getMenu: ({ menu }) => {
      return menu;
    }
  },
  mutations: {
    updateMiniIndex(state, index) {
      state.mini = index;
    },
    updateMenu(state, data) {
      state.menu = data;
    }
  },
  actions: {
    async fetchMenuData({ commit }, accountId) {
      const {
        data: { code, data }
      } = await api.team.getLeftNav(accountId);
      if (code === REQUEST.SUCCESS.code) commit("updateMenu", data);
    }
  }
};

export default left;
