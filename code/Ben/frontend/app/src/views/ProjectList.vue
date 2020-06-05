<template>
  <div class="project-list">
    <user-nav title="项目列表" :color="dark ? '' : '#5A94F2'">
      <Permission :perm="getProjectAdd">
        <v-btn outlined @click="handleOpenAddProjectTeamList">
          <v-icon class="mr-1">
            mdi-shape-square-plus
          </v-icon>
          添加项目
        </v-btn>
      </Permission>
      <Permission :perm="getProjectAdd">
        <add-team-before-project></add-team-before-project>
      </Permission>
    </user-nav>
    <Permission :perm="getProjectView">
      <chart
        v-if="rows.length > 0"
        tip="图表"
        title="本年每月创建项目数"
        subTitle="该图显示本公司每月创建团队数"
        yAxisName="项目总数"
        color="#FF5F6D"
        subColor="#FFC371"
        :rows="rows"
        column="项目总数"
      ></chart>
    </Permission>
    <v-divider class="mb-5"></v-divider>
    <Permission :perm="getProjectView">
      <project-table></project-table>
    </Permission>
  </div>
</template>

<script>
import { eventBus } from "../bus";
import { mapActions } from "vuex";
import { checkStorePermission } from "../utils/auth/auth";
import { PROJECT_PERM } from "../utils/permission";
import { REQUEST } from "../common/view/Constant";

export default {
  name: "ProjectList",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    addTeamBeforeProject: () =>
      import("../components/projectlist/addTeamDialogForNewProject.vue"),
    projectTable: () => import("../components/projectlist/projectTable.vue"),
    chart: () => import("../components/public/charts/countLineChart.vue")
  },
  data: () => ({
    rows: []
  }),
  methods: {
    ...mapActions({
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    handleOpenAddProjectTeamList() {
      eventBus.$emit("showAddTeamDialogForNewProjectDialog", true);
    },
    getRenderData(rows) {
      let data = [];
      rows.forEach(e => data.push({ 月份: e.date, 项目总数: e.count }));
      return data;
    }
  },
  computed: {
    company() {
      return this.$store.getters.getCompany;
    },
    dark() {
      return this.$store.getters.getDark;
    },
    getProjectAdd() {
      return PROJECT_PERM.ADD_PROJECT;
    },
    getProjectView() {
      return PROJECT_PERM.VIEW_PROJECT;
    }
  },
  async created() {
    const {
      data: { code, data }
    } = await this.$api.search.company.getMonthProjectChartData(
      this.company.id
    );
    if (code === REQUEST.SUCCESS.code) {
      this.rows = this.getRenderData(data);
    }
  },
  beforeRouteEnter(to, from, next) {
    const { perm } = to.meta;
    next(vm => {
      if (!checkStorePermission(vm.$store.getters.getPermission, perm))
        vm.$router.push("/403");
    });
  }
};
</script>
