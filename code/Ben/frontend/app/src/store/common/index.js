import { version } from "../../../package.json";
import api from "../../utils/api";
import { REQUEST } from "../../common/view/Constant";

const common = {
  state: {
    version: version,
    account: null,
    company: null,
    loading: false,
    dark: false
  },
  getters: {
    getVersion({ version }) {
      return version;
    },
    getAccount({ account }) {
      return account;
    },
    getPermission({ account }) {
      return account.permissions;
    },
    getCompany({ company }) {
      return company;
    },
    getLoading({ loading }) {
      return loading;
    },
    getDark({ dark }) {
      const index = localStorage.getItem("ben-theme");
      if (index) {
        return index === "true";
      } else {
        return dark;
      }
    }
  },
  mutations: {
    updateAccount(state, data) {
      state.account = data;
    },
    updateCompany(state, data) {
      state.company = data;
    },
    updateLoading(state, index) {
      state.loading = index;
    },
    updateDark(state, index) {
      state.dark = index;
      localStorage.setItem("ben-theme", index);
    }
  },
  actions: {
    async fetchAccount({ commit }, id) {
      const {
        data: { code, data }
      } = await api.account.getAccount(id);
      if (code === REQUEST.SUCCESS.code) commit("updateAccount", data);
    },
    async fetchCompany({ commit }, id) {
      const {
        data: { code, data }
      } = await api.company.getCompany(id);
      if (code === REQUEST.SUCCESS.code) commit("updateCompany", data);
    },
    async fetchUserInfo({ commit }) {
      const {
        data: { data, code }
      } = await api.company.getAccountAndCompanyByToken();
      if (code === REQUEST.SUCCESS.code) {
        let account = {
          id: data.accountId,
          name: data.accountName,
          email: data.email,
          gender: data.gender,
          phoneNumber: data.phoneNumber,
          avatarUrl: data.avatarUrl,
          createTime: data.createTime,
          admin: data.admin,
          role: data.role,
          permissions: data.permissionList
        };
        let company = {
          id: data.id,
          name: data.name,
          type: data.type,
          address: data.address,
          description: data.description,
          createTime: data.companyCreateTime,
          updateTime: data.companyUpdateTime
        };
        commit("updateCompany", company);
        commit("updateAccount", account);
      }
    }
  }
};

export default common;
