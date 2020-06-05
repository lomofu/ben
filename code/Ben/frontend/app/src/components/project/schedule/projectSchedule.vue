<template>
  <div>
    <user-nav :title="getTitle" color="#424242">
      <v-btn outlined class="mr-4" @click="handleSetToday">
        <v-icon small>mdi-white-balance-sunny</v-icon>
        <span class="ml-1">今天</span>
      </v-btn>

      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn fab text small @click="prev" v-on="on">
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
        </template>
        <span>{{ getPreTip }}</span>
      </v-tooltip>

      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn fab text small class="mr-3" @click="next" v-on="on">
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
        </template>
        <span>{{ getNextTip }}</span>
      </v-tooltip>

      <div v-show="!showSchedule">
        <v-menu bottom left>
          <template v-slot:activator="{ on }">
            <v-btn outlined v-on="on" class="mr-2">
              <span>{{ typeToLabel[type] }}</span>
              <v-icon right>mdi-menu-down</v-icon>
            </v-btn>
          </template>
          <v-list class="pa-1">
            <v-list-item @click="type = 'day'">
              <v-list-item-title>
                <v-icon>mdi-calendar-blank</v-icon>
                天
              </v-list-item-title>
            </v-list-item>
            <v-list-item @click="type = 'week'">
              <v-list-item-title>
                <v-icon>mdi-calendar-range</v-icon>
                周</v-list-item-title
              >
            </v-list-item>
            <v-list-item @click="type = 'month'">
              <v-list-item-title>
                <v-icon>mdi-calendar-month</v-icon>
                月份
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
      <Permission :perm="'sys:message:send'">
        <v-btn outlined class="mr-4" @click="handlePushSchedule">
          <v-icon small>mdi-arrow-up-bold-circle</v-icon>
          <span class="ml-2">推送</span>
        </v-btn>
      </Permission>
    </user-nav>
    <content-loading></content-loading>
    <div class="toolbar d-flex justify-space-between mt-5">
      <v-btn class="mt-2 ml-1" @click="handleBack" text color="purple">
        <v-icon>mdi-chevron-left</v-icon>返回
      </v-btn>
      <v-spacer></v-spacer>
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn
            class="mt-2 mr-4"
            icon
            @click="handleRefresh"
            outlined
            :loading="isDisable"
            color="purple"
            v-on="on"
          >
            <v-icon>mdi-refresh</v-icon>
          </v-btn>
        </template>
        <span>刷新</span>
      </v-tooltip>
      <v-btn-toggle
        color="teal"
        borderless
        class="pa-1 mr-7"
        v-model="toolbox_select"
        size
      >
        <v-btn
          @click="showSchedule ? showSchedule : (showSchedule = !showSchedule)"
        >
          <span class="hidden-sm-and-down">排班模式</span>
          <v-icon right>mdi-poll</v-icon>
        </v-btn>
        <v-btn
          @click="!showSchedule ? showSchedule : (showSchedule = !showSchedule)"
        >
          <span class="hidden-sm-and-down">日历模式</span>
          <v-icon right>mdi-calendar</v-icon>
        </v-btn>
      </v-btn-toggle>
    </div>
    <schedule
      ref="projectCalendar"
      v-if="showSchedule"
      :data="events"
      :account="list"
    >
      <v-btn v-if="hasNext" class="ml-2" small outlined @click="nextPage"
        >加载更多</v-btn
      >
    </schedule>
    <calendar
      ref="calendarApp"
      :type="type"
      v-else
      :data="events"
      :account="list"
      @changeTypeToDay="changeTypeToDay"
    ></calendar>
    <jobDialog></jobDialog>
    <pushDialog ref="push"></pushDialog>
    <push></push>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import { REQUEST } from "../../../common/view/Constant";
import { eventBus } from "../../../bus";
import { checkPermission } from "../../../utils/auth/auth";
import { JOB_PERM } from "../../../utils/permission";
export default {
  name: "projectSchedule",
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  components: {
    userNav: () => import("../../public/userNav.vue"),
    schedule: () => import("./schedule.vue"),
    calendar: () => import("./calendarApp.vue"),
    jobDialog: () => import("./job/jobDialog.vue"),
    pushDialog: () => import("./pushSchedule.vue"),
    contentLoading: () => import("../../public/contentLoading.vue"),
    push: () => import("../../dashboard/push.vue")
  },
  data: () => ({
    project: null,
    isDisable: false,
    showSchedule: true,
    toolbox_select: 0,
    hasNext: false,
    type: "month",
    typeToLabel: {
      month: "月份",
      week: "周",
      day: "天"
    },
    pageFilter: {
      pageNumber: 1,
      pageSize: 5,
      sortBy: "name",
      sortDesc: false,
      data: null
    },
    list: [],
    events: []
  }),
  methods: {
    ...mapMutations({
      updateShowSchedule: "updateShowSchedule",
      updateLoading: "updateLoading",
      updateJobList: "updateJobList"
    }),
    handleBack() {
      this.updateShowSchedule(false);
    },
    handleSetToday() {
      this.showSchedule
        ? this.$refs.projectCalendar.setToday()
        : this.$refs.calendarApp.setToday();
    },
    prev() {
      this.showSchedule
        ? this.$refs.projectCalendar.prev()
        : this.$refs.calendarApp.prev();
    },
    next() {
      this.showSchedule
        ? this.$refs.projectCalendar.next()
        : this.$refs.calendarApp.next();
    },
    changeTypeToDay() {
      this.type = "day";
    },
    handlePushSchedule() {
      this.$refs.push.dialog = true;
    },
    async fetchAccountList() {
      const {
        data: { code, data }
      } = await this.$api.account.getSimpleAccountListByProjectId(
        this.pageFilter
      );
      if (code === REQUEST.SUCCESS.code) {
        this.hasNext = data.hasNextPage;
        data.list.forEach(e => {
          this.list.push(e);
        });
      }
    },
    async fetchEventList() {
      this.updateLoading(true);
      try {
        const {
          data: { code, data }
        } = await this.$api.job.getEventListByProjectId(this.$route.params.id);
        if (code === REQUEST.SUCCESS.code) {
          this.events = data;
          this.updateJobList(data);
        }
      } finally {
        setTimeout(() => this.updateLoading(false), 300);
      }
    },
    async handleRefresh() {
      this.list = [];
      this.pageFilter.pageNumber = 1;
      this.isDisable = true;
      try {
        this.fetchAccountList();
        await this.fetchEventList();
      } finally {
        setTimeout(() => {
          this.isDisable = false;
        }, 300);
      }
    },
    async nextPage() {
      if (this.hasNext) {
        this.pageFilter.pageNumber++;
        this.isDisable = true;
        try {
          this.fetchAccountList();
          await this.fetchEventList();
        } finally {
          setTimeout(() => {
            this.isDisable = false;
          }, 300);
        }
      }
    }
  },
  computed: {
    getTitle() {
      return `项目 | ${this.project.name}`;
    },
    getPreTip() {
      if (this.toolbox_select === 0) return "上一周";
      if (this.type === "month") {
        return "上个月";
      }
      if (this.type === "day") {
        return "昨天";
      }
      return "上一周";
    },
    getNextTip() {
      if (this.toolbox_select === 0) return "下一周";
      if (this.type === "month") {
        return "下个月";
      }
      if (this.type === "day") {
        return "明天";
      }
      return "下一周";
    }
  },
  watch: {
    $route() {
      this.pageFilter.data = this.$route.params.id;
      this.fetchAccountList();
      this.fetchEventList();
    }
  },
  created() {
    if (checkPermission(JOB_PERM.VIEW_JOB)) {
      this.pageFilter.data = this.$route.params.id;
      this.project = this.data;
      this.fetchAccountList();
      this.fetchEventList();
      eventBus.$on("refreshEventList", () => {
        this.fetchEventList();
      });
    } else {
      this.$router.push("/403");
    }
  },
  beforeDestroy() {
    eventBus.$off("refreshEventList");
  }
};
</script>
