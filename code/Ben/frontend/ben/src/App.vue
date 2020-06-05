<template>
  <v-app>
    <div id="loading" v-if="loading">
      <load id="loading-animate" />
    </div>
    <keep-alive>
      <transition mode="out-in">
        <router-view v-if="success" />
      </transition>
    </keep-alive>
    <v-fab-transition>
      <v-btn
        v-show="navBtn.btnShow"
        color="red"
        dark
        fixed
        bottom
        right
        fab
        @click="$vuetify.goTo(0)"
      >
        <v-icon>{{ navBtn.btnIcon }}</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>
import debounce from "lodash/debounce";
export default {
  name: "App",
  components: {
    load: () => import("@/components/public/load.vue")
  },
  provide() {
    return {
      reload: this.reload
    };
  },
  data: () => ({
    loading: true,
    success: false,
    navBtn: {
      btnShow: false,
      btnIcon: "mdi-chevron-up"
    }
  }),
  methods: {
    handleScroll() {
      let me = this;
      let scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      me.navBtn.btnShow = scrollTop > 120;
    },
    reload() {
      this.success = false;
      this.$nextTick(function() {
        this.success = true;
      });
    }
  },
  created() {
    window.addEventListener("scroll", debounce(this.handleScroll, 300));
  },
  mounted() {
    let me = this;
    this.$nextTick(() => {
      this.timer = setTimeout(() => {
        me.loading = !me.loading;
        me.success = !me.success;
      }, 800);
    });
  },
  beforeDestroy() {
    clearTimeout(this.timer);
  }
};
</script>
<style lang="scss">
@import "@/assets/css/global/app.scss";
</style>
