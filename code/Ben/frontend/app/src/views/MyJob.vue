<template>
  <div>
    <user-nav title="我的排班" :color="dark ? '' : 'green'"> </user-nav>
    <countCard></countCard>
    <v-divider class="mt-5 mb-5"></v-divider>
    <thisWeekList></thisWeekList>
    <v-divider class="mt-5 mb-5"></v-divider>
    <totalJobList></totalJobList>
  </div>
</template>

<script>
export default {
  name: "MyJob",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    countCard: () => import("../components/myjob/countCard.vue"),
    thisWeekList: () => import("../components/myjob/thisWeekList.vue"),
    totalJobList: () => import("../components/myjob/totalJobList.vue")
  },
  computed: {
    dark() {
      return this.$store.getters.getDark;
    }
  }
};
</script>

<style scoped></style>
