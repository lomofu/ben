import Vue from "vue";
import Skeleton from "../components/public/load.vue";

export default new Vue({
  components: {
    Skeleton
  },
  render: h => h(Skeleton)
});
