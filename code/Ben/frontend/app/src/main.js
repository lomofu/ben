import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import Vuex from "vuex";
import vuetify from "./plugins/vuetify";
import axios from "axios";
import VueAxios from "vue-axios";
import Router from "vue-router";
import api from "./utils/api";
import moment from "./utils/moment";
import animated from "animate.css";
import "hover.css";
import VCharts from "v-charts";
import VueClipboard from "vue-clipboard2";
import info from "./utils/notification";
import VueCookies from "vue-cookies";
import Permission from "./components/public/Permission";
import GoEasy from "goeasy";

Vue.config.productionTip = false;
VueClipboard.config.autoSetContainer = true;
Vue.prototype.$api = api;
Vue.prototype.$moment = moment;
Vue.prototype.$notification = info;
Vue.prototype.$goEasy = new GoEasy({
  host: "hangzhou.goeasy.io",
  appkey: process.env.VUE_APP_GO_KEY,
  onConnected: function() {
    console.log("连接成功！");
  },
  onDisconnected: function() {
    console.log("连接断开！");
  },
  onConnectFailed: function(error) {
    console.log("连接失败或错误！", error);
  }
});

Vue.use(Vuex);
Vue.use(animated);
Vue.use(VCharts);
Vue.use(VueCookies);
Vue.use(VueClipboard);
Vue.use(VueAxios, axios);
Vue.component("Permission", Permission);

const domain = process.env.VUE_APP_NAME === "LOCAL" ? "" : ".lomofu.com";
Vue.$cookies.config("7d", "", domain);

const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

router.beforeEach((to, from, next) => {
  document.title = `APP.  |  ${to.meta.title}`;
  next();
});

const vue = new Vue({
  store,
  vuetify,
  router,
  render: h => h(App)
}).$mount("#app");

export default vue;
