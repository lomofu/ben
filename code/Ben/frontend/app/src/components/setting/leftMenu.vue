<template>
  <v-card>
    <v-navigation-drawer
      app
      clipped
      :color="dark ? '' : 'grey lighten-2'"
      width="210"
    >
      <v-list>
        <v-list-item
          v-for="item in getSetting"
          :key="item.id"
          link
          @click="$router.push(`/setting${item.url}`)"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.name }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <template v-slot:append>
        <div class="pa-2">
          <v-btn color="red" outlined block @click="handleLogOut"
            >退出登录</v-btn
          >
        </div>
      </template>
    </v-navigation-drawer>
  </v-card>
</template>

<script>
export default {
  name: "leftMenu",
  props: {
    dark: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleLogOut() {
      this.$notification.success("退出成功");
      this.$cookies.remove("ben-user");
      setTimeout(() => window.location.replace(process.env.VUE_APP_URL), 500);
    }
  },
  computed: {
    getSetting() {
      return this.$store.getters.getSetting;
    }
  }
};
</script>

<style scoped>
.v-navigation-drawer__border {
  display: none;
}
</style>
