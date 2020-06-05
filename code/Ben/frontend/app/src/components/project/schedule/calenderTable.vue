<template>
  <div class="d-flex">
    <table name="schedule">
      <th scope="col" v-for="(val, index) in 7" :key="`th-${index}`">
        <span :class="isToday(val)">
          {{ getNow(val).format("dddd Do") }}
        </span>
      </th>
      <tbody>
        <tr v-for="idx in member" :key="`tr-${idx}`">
          <td v-for="index in 7" :key="`td-${index}`">
            <v-card height="100%">
              <div
                v-if="isHaveEvent(index, idx)"
                :style="
                  getTodayEventList(index, idx).length > 1
                    ? 'overflow-y: auto;overflow-x:auto'
                    : null
                "
              >
                <div
                  class="flex-row pa-1"
                  v-for="(item, key) in getTodayEventList(index, idx)"
                  :key="key"
                >
                  <v-badge
                    v-if="!item.publish"
                    bordered
                    bottom
                    offset-y="18"
                    offset-x="18"
                    color="orange"
                    icon="mdi-arrow-up-bold"
                  >
                    <a-popover title="操作" placement="right">
                      <template slot="content">
                        <Permission :perm="getJobAdd">
                          <v-btn
                            text
                            style="color: dodgerblue"
                            @click="handleAddEvent(idx, index)"
                          >
                            <v-icon>
                              mdi-plus
                            </v-icon>
                            <span>添加新任务</span>
                          </v-btn>
                        </Permission>
                        <Permission :perm="getJobDel">
                          <a-popconfirm
                            title="你确定要删除这个任务吗?"
                            :visible="visible"
                            @confirm="confirm(item)"
                            @cancel="cancel"
                            okText="是的"
                            cancelText="取消"
                          >
                            <v-btn
                              text
                              style="color: red"
                              @click="visible = true"
                            >
                              <v-icon>
                                mdi-delete
                              </v-icon>
                              <span>删除此任务</span>
                            </v-btn>
                          </a-popconfirm>
                        </Permission>
                      </template>
                      <v-btn
                        small
                        height="40"
                        block
                        :color="item.color"
                        @click="handleShow(idx, item)"
                        dark
                      >
                        <div>
                          <p>
                            {{ getRenderItem(item) }}
                          </p>
                          <p>
                            <strong>{{ item.name }}</strong>
                          </p>
                        </div>
                      </v-btn>
                    </a-popover>
                  </v-badge>
                  <a-popover v-else title="操作" placement="right">
                    <template slot="content">
                      <Permission :perm="getJobAdd">
                        <v-btn
                          text
                          style="color: dodgerblue"
                          @click="handleAddEvent(idx, index)"
                        >
                          <v-icon>
                            mdi-plus
                          </v-icon>
                          <span>添加新任务</span>
                        </v-btn>
                      </Permission>
                      <Permission :perm="getJobDel">
                        <a-popconfirm
                          title="你确定要删除这个任务吗?"
                          :visible="visible"
                          @confirm="confirm(item)"
                          @cancel="cancel"
                          okText="是的"
                          cancelText="取消"
                        >
                          <v-btn
                            text
                            style="color: red"
                            @click="visible = true"
                          >
                            <v-icon>
                              mdi-delete
                            </v-icon>
                            <span>删除此任务</span>
                          </v-btn>
                        </a-popconfirm>
                      </Permission>
                    </template>
                    <v-btn
                      small
                      height="40"
                      block
                      :color="item.color"
                      @click="handleShow(idx, item)"
                      dark
                    >
                      <div>
                        <p>
                          {{ getRenderItem(item) }}
                        </p>
                        <p>
                          <strong>{{ item.name }}</strong>
                        </p>
                      </div>
                    </v-btn>
                  </a-popover>
                </div>
              </div>

              <div
                v-else
                class="event d-flex justify-center"
                v-ripple
                style="cursor: pointer"
                @click="handleAddEvent(idx, index)"
              >
                <Permission :perm="getJobAdd">
                  <v-icon size="70">
                    mdi-plus-circle-outline
                  </v-icon>
                </Permission>
              </div>
            </v-card>
          </td>
        </tr>
      </tbody>
      <caption class="ml-5">
        任务排班表
      </caption>
    </table>

    <slot></slot>
  </div>
</template>

<script>
import { Popconfirm, Popover } from "ant-design-vue";
import { mapGetters } from "vuex";
import { REQUEST, TIME } from "../../../common/view/Constant";
import { eventBus } from "../../../bus";
import { checkPermission } from "../../../utils/auth/auth";
import { JOB_PERM } from "../../../utils/permission";
export default {
  name: "calenderTable",
  props: {
    member: {
      type: Number,
      required: true,
      default: 0
    },
    data: {
      type: Array,
      required: true,
      default: function() {
        return [];
      }
    },
    account: {
      type: Array,
      required: true,
      default: function() {
        return [];
      }
    },
    startDate: {
      type: String,
      required: true,
      default: function() {
        return "";
      }
    }
  },
  components: {
    aPopconfirm: Popconfirm,
    aPopover: Popover
  },
  data: () => ({
    visible: false
  }),
  methods: {
    getNow(index) {
      return this.$moment(this.startDate).add(index - 1, TIME.DAY);
    },
    getTodayEventList(index, idx) {
      const accountId = this.getAccount(idx).id;
      let list = this.data;
      let now = this.getNow(index);
      return list.filter(
        e =>
          now.isSameOrAfter(this.$moment(e.start), TIME.DAY) &&
          now.isSameOrBefore(this.$moment(e.end), TIME.DAY) &&
          e.accountId === accountId
      );
    },
    getRenderItem(item) {
      const start = this.$moment(item.start);
      const end = this.$moment(item.end);
      if (start.isSame(end, TIME.DAY)) {
        if (item.full) {
          return `${start.format(TIME.SIMPLE_DAY)} 全天`;
        }
        return `${start.format(TIME.SIMPLE_DAY)} ${start.format(
          TIME.SIMPLE_LOCAL_TIME
        )}-${end.format(TIME.SIMPLE_LOCAL_TIME)}`;
      }
      if (item.full) {
        return `${start.format(TIME.SIMPLE_DAY)}-${end.format(
          TIME.SIMPLE_DAY
        )}`;
      }
      return `${start.format(TIME.SIMPLE_MONTH_AND_LOCAL_TIME)}-${end.format(
        TIME.SIMPLE_MONTH_AND_LOCAL_TIME
      )}`;
    },
    getAccount(idx) {
      return this.account[idx - 1];
    },
    handleShow(idx, item) {
      eventBus.$emit("showEventDialog", {
        type: null,
        edit: false,
        data: {
          formData: item,
          account: this.getAccount(idx)
        }
      });
    },
    handleAddEvent(idx, index) {
      if (checkPermission(JOB_PERM.ADD_JOB)) {
        eventBus.$emit("showEventDialog", {
          type: "new",
          edit: true,
          data: this.getAccount(idx),
          start: this.getNow(index)
        });
      }
    },
    isToday(index) {
      return this.$moment().isSame(this.getNow(index), TIME.DAY)
        ? "today"
        : null;
    },
    isHaveEvent(index, idx) {
      return this.getTodayEventList(index, idx).length > 0;
    },
    confirm(item) {
      this.visible = false;
      this.handleDelete(item);
    },
    cancel() {
      this.visible = false;
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
    }
  },
  computed: {
    ...mapGetters({
      start: "getStart"
    }),
    getJobAdd() {
      return JOB_PERM.ADD_JOB;
    },
    getJobDel() {
      return JOB_PERM.DEL_JOB;
    }
  }
};
</script>

<style scoped>
div {
  margin: 0;
}
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 16px 50px;
}
th {
  text-align: center;
  min-width: 145px;
  max-width: 150px;
}
td {
  text-align: center;
  min-width: 175px;
  height: 150px;
}
.v-badge {
  width: 100%;
}
.item {
  width: 100%;
}
.today {
  background: #2196f3;
  color: white;
  border-radius: 100px;
  padding: 10px;
}
.event {
  width: 175px;
  height: 160px;
}
</style>
