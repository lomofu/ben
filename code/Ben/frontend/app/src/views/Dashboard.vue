<template>
  <div>
    <notice />
    <user-nav title="个人看板" :color="dark ? '' : 'primary'">
      <Permission :perm="getSendMsg">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" text @click="handleNotice" :loading="loading">
              <v-icon>mdi-bulletin-board</v-icon>
            </v-btn>
          </template>
          <span class="pa-3">推送通知</span>
        </v-tooltip>
      </Permission>
      <Permission :perm="getSendMsg">
        <push></push>
      </Permission>
    </user-nav>
    <v-row
      class="first"
      :style="{
        'min-height': '400px',
        background: dark ? '#0a0a0a' : ''
      }"
      justify="center"
    >
      <Permission :perm="getCompanyView">
        <v-col
          cols="12"
          xs="12"
          sm="12"
          md="6"
          lg="6"
          xl="6"
          align-self="center"
        >
          <company-info-card></company-info-card>
        </v-col>
      </Permission>

      <v-spacer></v-spacer>
      <v-divider vertical />
      <v-col
        cols="12"
        xs="12"
        sm="12"
        md="5"
        lg="5"
        xl="5"
        align-self="center"
        class="pa-3"
      >
        <count-card-list />
      </v-col>
    </v-row>
    <v-divider />
    <Permission :perm="getOverChartView">
      <v-row>
        <v-col class="xs" cols="12" sm="12" md="12" lg="12" xl="12">
          <overview-chart />
        </v-col>
      </v-row>
    </Permission>
    <Permission :perm="getOverChartView">
      <v-divider />
      <v-row style="background: #f8f8f8">
        <v-col class="col-12 d-flex justify-center">
          <tree :dark="dark" />
        </v-col>
      </v-row>
    </Permission>
  </div>
</template>

<script>
import { eventBus } from "../bus";
import { mapGetters, mapActions } from "vuex";
import { CHART_PERM, COMPANY_PERM, SEND_PERM } from "../utils/permission";

export default {
  name: "Dashboard",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    countCardList: () => import("../components/dashboard/countCardList.vue"),
    overviewChart: () => import("../components/dashboard/overviewChart.vue"),
    companyInfoCard: () =>
      import("../components/dashboard/companyInfoCard.vue"),
    tree: () => import("../components/dashboard/tree.vue"),
    notice: () => import("../components/dashboard/notice.vue"),
    push: () => import("../components/dashboard/push.vue")
  },
  methods: {
    handleNotice() {
      eventBus.$emit("showNoticeDialog", true);
    },
    ...mapActions({
      fetchTotalCount: "fetchTotalCount"
    })
  },
  data: () => ({
    loading: false
  }),
  computed: {
    ...mapGetters({
      account: "getAccount",
      company: "getCompany",
      dark: "getDark"
    }),
    getCompanyView() {
      return COMPANY_PERM.VIEW_COMPANY;
    },
    getSendMsg() {
      return SEND_PERM.SEND_MSG;
    },
    getOverChartView() {
      return CHART_PERM.VIEW_OVER_CHART;
    }
  },
  created() {
    this.fetchTotalCount({
      accountId: this.account.id,
      companyId: this.company.id
    });
    eventBus.$on("pushing", index => {
      this.loading = index;
    });
  },
  beforeDestroy() {
    eventBus.$off("pushing");
  }
};
</script>

<style lang="scss" scoped>
@mixin project {
  background: #005c97;
  background: -webkit-linear-gradient(to right, #005c97, #363795);
  background: linear-gradient(to right, #005c97, #363795);
}

@mixin team {
  background: #ff8008;
  background: -webkit-linear-gradient(to right, #ffc837, #ff8008);
  background: linear-gradient(to right, #ffc837, #ff8008);
}

@mixin member {
  background: #6a3093;
  background: -webkit-linear-gradient(to right, #863fde, #6a3093);
  background: linear-gradient(to right, #863fde, #6a3093);
}

div {
  margin: 0;
}
p {
  color: white;
}
.first {
  background: #fcfcfc;
  background: -webkit-linear-gradient(to right, #fcfcfc, #ffff);
  background: linear-gradient(to right, #fcfcfc, #ffff);
}
.myteam {
  @include font-bold;
  font-size: 20px;
  color: black;
}

.count {
  @include font-bold;
  font-size: 30px;
}
.project-box {
  @include project;
}

.team-box {
  @include member;
}

.member-box {
  @include team;
  text-shadow: $text-shadow;
}

.treeMap {
  width: 90%;
}
</style>
