<template>
  <div class="team-list" ref="TeamList">
    <user-nav title="团队列表" :color="dark ? '' : '#BE4CC4'">
      <Permission :perm="getTeamAdd">
        <v-btn outlined @click="handleAddTeam">
          <v-icon class="mr-1">
            mdi-account-multiple-plus-outline
          </v-icon>
          添加团队
        </v-btn>
      </Permission>
    </user-nav>
    <Permission :perm="getTeamView">
      <v-row>
        <v-col class="xs" cols="12" sm="12" md="12" lg="12" xl="12">
          <chart
            v-if="rows.length > 0"
            tip="图表"
            title="本年每月创建团队数"
            subTitle="该图显示本公司每月创建团队数"
            yAxisName="团队总数"
            :rows="rows"
            column="团队总数"
          ></chart>
        </v-col>
      </v-row>
    </Permission>
    <v-divider class="mb-5" />
    <Permission :perm="getTeamView">
      <team-table></team-table>
    </Permission>
  </div>
</template>

<script>
import { eventBus } from "../bus";
import { checkStorePermission } from "../utils/auth/auth";
import { TEAM_PERM } from "../utils/permission";
import { REQUEST } from "../common/view/Constant";

export default {
  name: "TeamList",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    chart: () => import("../components/public/charts/countLineChart.vue"),
    teamTable: () => import("../components/teamlist/teamTable.vue")
  },
  data: () => ({
    rows: []
  }),
  methods: {
    handleAddTeam() {
      eventBus.$emit("showTeamDialog", { type: "create", index: true });
    },
    getRenderData(rows) {
      let data = [];
      rows.forEach(e => data.push({ 月份: e.date, 团队总数: e.count }));
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
    getTeamAdd() {
      return TEAM_PERM.ADD_TEAM;
    },
    getTeamView() {
      return TEAM_PERM.VIEW_TEAM;
    }
  },
  async created() {
    const {
      data: { code, data }
    } = await this.$api.search.company.getMonthTeamChartData(this.company.id);
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

<style scoped></style>
