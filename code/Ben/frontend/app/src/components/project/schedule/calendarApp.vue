<template>
  <div class="fadeIn">
    <v-toolbar-title class="text-center">{{ title }}</v-toolbar-title>
    <div class="d-flex justify-start">
      <v-col class="col-3">
        <v-sheet height="300" class="mt-2 ml-3">
          <v-date-picker
            header-color="#FFB300"
            color="black"
            v-model="focus"
            :picker-date.sync="pickerDate"
            reactive
            :first-day-of-week="0"
            locale="zh-cn"
            full-width
          ></v-date-picker>
        </v-sheet>
      </v-col>
      <v-col class="col-9">
        <v-sheet height="700" class="pa-2 mr-3">
          <v-calendar
            ref="calendar"
            v-model="focus"
            color="primary"
            :events="data"
            :event-color="getEventColor"
            :now="today"
            :type="type"
            @click:event="showEvent"
            @click:more="viewDay"
            @click:date="viewDay"
            @change="updateRange"
            locale="zh-cn"
            :short-intervals="false"
          ></v-calendar>
          <v-menu
            v-if="selectedOpen"
            v-model="selectedOpen"
            :close-on-content-click="false"
            :activator="selectedElement"
            max-width="450px"
            min-width="400px"
          >
            <v-card flat>
              <v-toolbar
                prominent
                flat
                dense
                :color="getEventColor(selectedEvent)"
                dark
              >
                <v-toolbar-title
                  class="d-flex offset-1"
                  v-html="selectedEvent.name"
                ></v-toolbar-title>

                <template v-slot:extension>
                  <Permission :perm="getJobEdit">
                    <v-tooltip bottom>
                      <template v-slot:activator="{ on }">
                        <v-btn
                          color="grey"
                          :elevation="0"
                          small
                          absolute
                          bottom
                          left
                          fab
                          v-on="on"
                          @click="handleEditSelectEvent(selectedEvent)"
                        >
                          <v-icon>mdi-pencil</v-icon>
                        </v-btn>
                      </template>
                      <span>编辑</span>
                    </v-tooltip>
                  </Permission>
                </template>
                <v-spacer></v-spacer>

                <Permission :perm="getJobDel">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <a-popconfirm
                        title="你确定要删除这个任务吗?"
                        :visible="visible"
                        @confirm="confirm(selectedEvent)"
                        @cancel="cancel"
                        okText="是的"
                        cancelText="取消"
                      >
                        <v-btn icon v-on="on" @click="visible = true">
                          <v-icon>mdi-delete</v-icon>
                        </v-btn>
                      </a-popconfirm>
                    </template>
                    <span>删除</span>
                  </v-tooltip>
                </Permission>

                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn icon v-on="on" @click="selectedOpen = false">
                      <v-icon>mdi-close</v-icon>
                    </v-btn>
                  </template>
                  <span>关闭</span>
                </v-tooltip>
              </v-toolbar>
              <v-card-text class="mt-3">
                <div class="d-flex justify-start">
                  <v-icon class="d-flex offset-1">mdi-account</v-icon>
                  <span class="d-flex offset-2">
                    {{ getAccount(selectedEvent.accountId).name }}
                  </span>
                </div>
                <br />
                <div class="d-flex justify-start">
                  <v-icon class="d-flex offset-1">mdi-message-text</v-icon>
                  <span
                    class="d-flex offset-2"
                    v-html="
                      selectedEvent.description === null ||
                      selectedEvent.description === undefined
                        ? '暂无描述'
                        : selectedEvent.description
                    "
                  ></span>
                </div>
                <br />
                <div class="d-flex justify-start">
                  <v-icon class="d-flex offset-1">mdi-clock-outline</v-icon>
                  <span class="d-flex offset-2">
                    {{ getRangeItemTime(selectedEvent) }}
                  </span>
                </div>
              </v-card-text>
            </v-card>
          </v-menu>
        </v-sheet>
      </v-col>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapGetters } from "vuex";
import { eventBus } from "../../../bus";
import { Popconfirm } from "ant-design-vue";
import { REQUEST } from "../../../common/view/Constant";
import { JOB_PERM } from "../../../utils/permission";

export default {
  name: "calendarApp",
  props: {
    data: {
      type: Array,
      required: true,
      default: function() {
        return [];
      }
    },
    type: {
      type: String,
      required: true,
      default: "month"
    },
    account: {
      type: Array,
      required: true,
      default: function() {
        return [];
      }
    }
  },
  components: {
    aPopconfirm: Popconfirm
  },
  data: () => ({
    focus: null,
    today: null,
    visible: false,
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    pickerDate: null
  }),
  methods: {
    ...mapMutations({
      updateStart: "updateStart",
      updateEnd: "updateEnd"
    }),
    getEventColor(event) {
      return event.color;
    },
    getRangeItemTime(event) {
      const start = this.$moment(event.start).format(
        this.getFormat(event.full)
      );
      const end = this.$moment(event.end).format(this.getFormat(event.full));
      return start === end ? `${start} 全天` : `${start} - ${end}`;
    },
    getFormat(isFullDay) {
      return isFullDay ? "YYYY-MM-DD" : "YYYY-MM-DD HH:mm";
    },
    getAccount(accountId) {
      const account = this.account.find(e => e.id === accountId);
      return account === undefined || account === null
        ? { name: "未知" }
        : account;
    },
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
    },
    showEvent({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event;
        this.selectedElement = nativeEvent.target;
        setTimeout(() => (this.selectedOpen = true), 10);
      };

      if (this.selectedOpen) {
        this.selectedOpen = false;
        setTimeout(open, 10);
      } else {
        open();
      }
      nativeEvent.stopPropagation();
    },
    viewDay({ date }) {
      this.focus = date;
      this.$emit("changeTypeToDay");
    },
    handleEditSelectEvent(event) {
      eventBus.$emit("showEventDialog", {
        type: "edit",
        edit: true,
        data: {
          formData: event,
          account: this.getAccount(event.accountId)
        }
      });
    },
    confirm(item) {
      this.visible = false;
      this.handleDelete(item);
    },
    async handleDelete(item) {
      try {
        const {
          data: { code, msg }
        } = await this.$api.job.deleteJob(item.id);
        if (code === REQUEST.SUCCESS.code) {
          this.$notification.success(code, msg);
          eventBus.$emit("refreshEventList");
        }
      } finally {
        this.visible = false;
      }
    },
    cancel() {
      this.visible = false;
    }
  },
  computed: {
    ...mapGetters({
      getStart: "getStart",
      getEnd: "getEnd"
    }),
    title() {
      const start = this.getStart;
      const end = this.getEnd;
      if (!start || !end) {
        return "";
      }

      const startYear = start.year;
      const endYear = end.year;

      const startMonth = start.month;
      const endMonth = end.month;

      const startDay = start.day;
      const endDay = end.day;

      switch (this.type) {
        case "month":
          return `${startYear}年 ${startMonth}月 `;
        case "week":
          return `${startYear}年${startMonth}月${startDay}日  -  ${endYear}年${endMonth}月${endDay}日`;
        case "day":
          return `${startYear}年 ${startMonth}月${startDay}日 `;
      }
      return "";
    },
    getJobDel() {
      return JOB_PERM.DEL_JOB;
    },
    getJobEdit() {
      return JOB_PERM.EDIT_JOB;
    }
  }
};
</script>

<style scoped>
.v-calendar {
  background-color: rgba(255, 248, 166, 0.14) !important;
}
.v-date-picker-title {
  color: black !important;
}
.v-outside {
  background-color: #effaff !important;
}
</style>
