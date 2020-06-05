<template>
  <div>
    <content-loading></content-loading>
    <transition enter-active-class="animated fadeIn" mode="out-in">
      <project-index v-if="!show" :data="project"></project-index>
    </transition>
    <transition enter-active-class="animated fadeIn" mode="out-in">
      <project-schedule v-if="show" :data="project"></project-schedule>
    </transition>
  </div>
</template>

<script>
import { REQUEST } from "../common/view/Constant";
import { eventBus } from "../bus";
import { mapActions, mapGetters, mapMutations } from "vuex";
import { checkStorePermission } from "../utils/auth/auth";

export default {
  name: "Project",
  components: {
    projectIndex: () =>
      import("../components/project/projectIndex/projectIndex.vue"),
    projectSchedule: () =>
      import("../components/project/schedule/projectSchedule.vue"),
    contentLoading: () => import("../components/public/contentLoading.vue")
  },
  data: () => ({
    project: null
  }),
  methods: {
    ...mapMutations({
      updateShowSchedule: "updateShowSchedule",
      updateLoading: "updateLoading"
    }),
    ...mapActions({
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    async fetchProjectInfo() {
      this.updateLoading(true);
      this.updateShowSchedule(false);
      const { id } = this.$route.params;
      try {
        const {
          data: { code, data }
        } = await this.$api.project.getProjectByProjectId(id);
        if (code === REQUEST.SUCCESS.code) {
          this.project = data;
        }
      } finally {
        setTimeout(() => this.updateLoading(false), 300);
      }
    }
  },
  computed: {
    ...mapGetters({
      account: "getAccount",
      show: "getShowSchedule"
    })
  },
  watch: {
    $route() {
      this.fetchProjectInfo();
    }
  },
  beforeRouteEnter(to, from, next) {
    const { perm } = to.meta;
    next(vm => {
      if (!checkStorePermission(vm.$store.getters.getPermission, perm))
        vm.$router.push("/403");
      vm.updateShowSchedule(false);
    });
  },
  created() {
    this.fetchProjectInfo();
    eventBus.$on("reloadProjectInfo", () => {
      this.fetchProjectInfo();
    });
  },
  beforeRouteLeave(to, from, next) {
    this.$store.commit("updateFormData", null);
    this.$store.commit("updateJob", null);
    next();
  },
  beforeDestroy() {
    eventBus.$off("reloadProjectInfo");
  }
};
</script>

<style scoped>
.no-info {
  width: 100vw;
  height: 100vh;
}
</style>
