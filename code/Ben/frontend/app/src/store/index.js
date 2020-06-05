import Vue from "vue";
import Vuex from "vuex";
import common from "./common/index";
import left from "./left/index";
import dashboard from "./dashboard";
import teamList from "./teamlist";
import project from "./project";
import employee from "./employee";
import job from "./job";
import search from "./search";
import role from "./role";
import perm from "./perm";
import myjob from "./myjob";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    common,
    left,
    dashboard,
    teamList,
    project,
    employee,
    job,
    search,
    role,
    perm,
    myjob
  }
});
