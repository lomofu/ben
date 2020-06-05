<template>
  <div :style="{ background: dark ? '#141414' : '' }">
    <v-card-title class="text-center justify-center control-panel-title pa-3">
      控制面板
    </v-card-title>
    <v-tabs v-model="tab" grow>
      <v-tab v-for="(item, idx) in getItems" :key="idx">
        <v-badge
          inline
          v-if="item === '消息中心' && count"
          color="green"
          :content="count"
        >
          {{ item }}
        </v-badge>
        <div v-else>
          {{ item }}
        </div>
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab" :style="{ background: dark ? '#141414' : '' }">
      <v-tab-item class="pa-3">
        <Permission :perm="getAccountView">
          <account-card></account-card>
        </Permission>
      </v-tab-item>
      <v-tab-item>
        <showDetailDialog></showDetailDialog>
        <timelines @updateCount="updateCount"></timelines>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import { ACCOUNT_PERM } from "../../../utils/permission";
const items = ["个人信息", "消息中心"];

export default {
  name: "userTab",
  props: ["user"],
  components: {
    accountCard: () => import("./accountCard.vue"),
    timelines: () => import("./timelines.vue"),
    showDetailDialog: () => import("./showNotificationDetail.vue")
  },
  data: () => ({
    tab: null,
    count: null
  }),
  methods: {
    updateCount(val) {
      this.count = val;
    }
  },
  computed: {
    getItems() {
      return items;
    },
    getAccountView() {
      return ACCOUNT_PERM.VIEW_ACCOUNT;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  }
};
</script>

<style scoped lang="scss">
.control-panel-title {
  @include font-bold;
  color: #323232;
}
</style>
