import "babel-polyfill";
import "animate.css";
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import Router from "vue-router";
import Vuex from "vuex";
import vuetify from "./plugins/vuetify";
import axios from "axios";
import VueAxios from "vue-axios";
import api from "./utils/api";
import msg from "./utils/message";
import VueCookies from "vue-cookies";
import "hover.css";

Vue.config.productionTip = false;
Vue.prototype.$api = api;
Vue.prototype.$msg = msg;

Vue.use(Vuex);
Vue.use(VueCookies);
Vue.use(VueAxios, axios);
const domain = process.env.VUE_APP_NAME === "LOCAL" ? "" : ".lomofu.com";
Vue.$cookies.config("7d", "", domain);

const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

router.beforeEach((to, from, next) => {
  document.title = "Ben. " + " | " + to.meta.title;
  next();
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
  mounted() {
    document.dispatchEvent(new Event("render-event"));
  }
}).$mount("#app");
