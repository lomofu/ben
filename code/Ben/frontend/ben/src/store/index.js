import Vue from "vue";
import Vuex from "vuex";
import { version } from "../../package.json";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    version: version,
    signUp: {
      showUnSignUpView: true
    },
    register: {
      showDoneView: false,
      email: null
    },
    employee: {
      showDoneView: false,
      info: null
    },
    login: {
      isPassword: false
    }
  },
  getters: {
    getVersion({ version }) {
      return version;
    },
    getShowUnSignUpView({ signUp: { showUnSignUpView } }) {
      return showUnSignUpView;
    },
    getRegisterDoneView({ register: { showDoneView } }) {
      return showDoneView;
    },
    getEmployeeDoneView({ employee: { showDoneView } }) {
      return showDoneView;
    },
    getEmployeeInfo({ employee: { info } }) {
      return info;
    },
    getEmail({ register: { email } }) {
      return email;
    },
    getIsPassword({ login: { isPassword } }) {
      return isPassword;
    }
  },
  mutations: {
    updateShowUnSignUpView({ signUp }, index) {
      signUp.showUnSignUpView = index;
    },
    updateRegisterDoneView({ register }, index) {
      register.showDoneView = index;
    },
    updateEmployeeDoneView({ employee }, index) {
      employee.showDoneView = index;
    },
    updateEmail({ register }, email) {
      register.email = email;
    },
    updateInfo({ employee }, info) {
      employee.info = info;
    },
    updateIsPassword({ login }, index) {
      login.isPassword = index;
    }
  },
  actions: {
    asyncUpdateShowUnSignUpView({ commit }, index) {
      commit("updateShowUnSignUpView", index);
    },
    asyncUpdateRegisterDoneView({ commit }, index) {
      commit("updateRegisterDoneView", index);
    }
  }
});
