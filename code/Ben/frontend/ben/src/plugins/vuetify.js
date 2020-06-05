import Vue from "vue";
import Vuetify from "vuetify/lib";
import "@mdi/font/css/materialdesignicons.css";
import VueTypedJs from "vue-typed-js";

Vue.use(Vuetify);
Vue.use(VueTypedJs);

export default new Vuetify({
  icons: {
    iconfont: "mdi"
  }
});
