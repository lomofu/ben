<template>
  <div class="fadeIn setting">
    <v-app-bar app clipped-left :color="dark ? '' : 'primary'" dark flat>
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on" @click="$router.push('/')">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <v-toolbar-title>设置</v-toolbar-title>
        </template>
        <span>返回主页</span>
      </v-tooltip>
      <v-spacer></v-spacer>
    </v-app-bar>
    <leftMenu :dark="dark"></leftMenu>
    <v-content :style="{ background: dark ? 'black' : '' }">
      <v-container fluid>
        <v-breadcrumbs :items="getBreadNav"></v-breadcrumbs>
        <transition
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut"
          mode="out-in"
        >
          <router-view></router-view>
        </transition>
      </v-container>
    </v-content>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Setting",
  components: {
    leftMenu: () => import("../components/setting/leftMenu.vue")
  },
  methods: {
    ...mapActions({
      fetchSettingMenu: "fetchSettingMenu"
    }),
    converseNav(text, disable, path) {
      return {
        text: text,
        disabled: disable,
        href: path
      };
    }
  },
  computed: {
    ...mapGetters({
      account: "getAccount"
    }),
    getBreadNav() {
      const nav = [];
      const { meta, path } = this.$route;
      nav.push(this.converseNav(meta.base, true, "/setting"));
      nav.push(this.converseNav(meta.title, meta.disabled, path));
      return nav;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  },
  created() {
    this.fetchSettingMenu(this.account.id);
  }
};
</script>

<style scoped>
.setting {
  margin: 0;
  height: 100%;
}
</style>
