<template>
  <div>
    <v-toolbar
      :color="dark ? '#0a0a0a' : ''"
      :dark="dark"
      flat
      class="d-flex offset-2 mb-3"
    >
      <v-toolbar-title>{{ title }}</v-toolbar-title>
    </v-toolbar>
    <v-row>
      <v-spacer></v-spacer>
      <v-col class="col-10">
        <v-calendar
          ref="calendar"
          v-model="focus"
          :now="today"
          type="week"
          @change="updateRange"
          v-show="false"
        ></v-calendar>
      </v-col>
    </v-row>
    <v-row v-if="data.length > 0">
      <v-spacer></v-spacer>
      <v-col class="col-10">
        <div style="width: 98%">
          <v-alert
            class="pa-5"
            v-model="alert"
            close-text="Close Alert"
            color="orange"
            dismissible
            :dark="dark"
            :outlined="!dark"
          >
            如何任务右下方含有
            <v-icon color="orange">mdi-arrow-up-bold-circle</v-icon>,
            这意味着该任务并未推送给目标用户,请确保该任务为本周内任务,然后点击右上角的推送按钮进行推送
          </v-alert>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col class="col-2">
        <table>
          <th :style="dark ? { color: 'black' } : { color: 'white' }">.</th>
          <tbody>
            <tr v-for="(item, index) in account" :key="index">
              <template>
                <td class="pa-2">
                  <div class="d-flex justify-center align-center">
                    <v-hover v-slot:default="{ hover }">
                      <v-badge
                        :value="hover"
                        :color="item.admin ? 'deep-purple' : 'orange'"
                        :content="item.admin ? '管理员' : '成员'"
                        left
                        transition="slide-x-transition"
                      >
                        <v-avatar size="40">
                          <v-img
                            :src="item.avatarUrl"
                            :lazy-src="item.avatarUrl"
                            :alt="item.name"
                          >
                            <template v-slot:placeholder>
                              <v-row
                                class="fill-height ma-0"
                                align="center"
                                justify="center"
                              >
                                <v-progress-circular
                                  indeterminate
                                  color="amber"
                                ></v-progress-circular>
                              </v-row>
                            </template>
                          </v-img>
                        </v-avatar>
                      </v-badge>
                    </v-hover>
                    <p class="ml-2">{{ item.name }}</p>
                  </div>
                </td>
              </template>
            </tr>
          </tbody>
          <slot></slot>
        </table>
      </v-col>
      <v-col class="col-10 pa-1" style="overflow-x:auto;">
        <calendarTable
          class="ma-3"
          :startDate="startDate"
          :member="account.length"
          :data="data"
          :account="account"
        >
        </calendarTable>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
export default {
  name: "schedule",
  components: {
    calendarTable: () => import("../schedule/calenderTable.vue")
  },
  props: {
    data: {
      type: Array,
      required: false,
      default: function() {
        return [];
      }
    },
    account: {
      type: Array,
      required: false,
      default: function() {
        return [];
      }
    }
  },
  data: () => ({
    hover: false,
    today: null,
    start: null,
    end: null,
    focus: null,
    alert: true
  }),
  methods: {
    ...mapMutations({
      updateStart: "updateStart",
      updateEnd: "updateEnd"
    }),
    setToday() {
      this.focus = this.today;
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    updateRange({ start, end }) {
      this.start = start;
      this.end = end;
      this.updateStart(start);
      this.updateEnd(end);
    }
  },
  computed: {
    title() {
      const { start, end } = this;
      if (!start || !end) {
        return "";
      }
      const startMonth = this.start.month;
      const endMonth = this.end.month;

      const startYear = start.year;
      const endYear = end.year;

      const startDay = start.day;
      const endDay = end.day;
      return `${startYear}年${startMonth}月${startDay}日 - ${endYear}年${endMonth}月${endDay}日 `;
    },
    dark() {
      return this.$store.getters.getDark;
    },
    startDate() {
      const { start, end } = this;
      if (!start || !end) {
        return "";
      }
      const startYear = start.year;
      const startMonth = this.start.month;
      const startDay = start.day;
      return `${startYear}-${startMonth}-${startDay}`;
    }
  }
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 2.8rem 3.5rem;
}
p {
  width: 70px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
td {
  width: 300px;
  height: 18vh;
  padding-right: 10px;
}
.col-2 {
  flex: 0 0 13.666666% !important;
}
.col-10 {
  flex: 0 0 82.333333% !important;
}
</style>
