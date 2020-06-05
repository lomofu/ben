<template>
  <div class="employeeTable">
    <v-toolbar
      dense
      class="ma-2"
      max-width="99%"
      flat
      :color="dark ? '#0a0a0a' : ''"
      :dark="dark"
    >
      <v-toolbar-title
        >{{ active ? "正式成员" : " 未激活员工" }}
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <Permission v-if="edit" :perm="getEditEmployeePerm">
        <v-btn
          outlined
          class="ma-3"
          color="primary"
          :disabled="selected.length !== 1"
          @click="handleEditAccount"
        >
          <v-icon class="mr-1">mdi-pencil-outline</v-icon>
          编辑
        </v-btn>
      </Permission>
      <Permission v-if="getShowRemove" :perm="getRemoveEmployeePerm">
        <v-btn
          outlined
          class="mr-3"
          color="purple"
          :disabled="selected.length <= 0"
          @click="showRemoveDialog"
        >
          <v-icon class="mr-1">mdi-account-minus</v-icon>
          移除成员
        </v-btn>
      </Permission>
      <Permission v-if="edit" :perm="getDelEmployeePerm">
        <v-btn
          outlined
          color="red"
          :disabled="selected.length <= 0"
          @click="showDeleteConfirm"
        >
          <v-icon class="mr-1">mdi-delete-outline</v-icon>
          删除
        </v-btn>
      </Permission>
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn
            v-on="on"
            outlined
            class="ma-3"
            color="green"
            @click="fetchData"
          >
            <v-icon>mdi-refresh</v-icon>
          </v-btn>
        </template>
        <span>刷新</span>
      </v-tooltip>
    </v-toolbar>
    <v-card outlined max-width="98%" class="ma-2" flat>
      <v-data-table
        v-model="selected"
        :calculate-widths="true"
        :headers="headers"
        :items="pageInfo.data"
        :loading="loading"
        item-key="id"
        :options.sync="options"
        :server-items-length="pageInfo.total"
        :items-per-page.sync="pageFilter.pageSize"
        :page.sync="pageFilter.pageNumber"
        hide-default-footer
        :show-select="showSelect"
      >
        <template v-slot:item.avatarUrl="{ item }">
          <v-avatar
            size="30"
            @click.stop="handleClickAvartar(item)"
            style="cursor: pointer"
          >
            <v-img
              :src="item.avatarUrl"
              :lazy-src="item.avatarUrl"
              :alt="item.name"
            >
              <template v-slot:placeholder>
                <v-row class="fill-height ma-0" align="center" justify="center">
                  <v-progress-circular
                    indeterminate
                    color="amber"
                  ></v-progress-circular>
                </v-row>
              </template>
            </v-img>
          </v-avatar>
        </template>
        <template v-slot:item.gender="{ item }">
          <a-tooltip
            v-if="item.gender === 1"
            placement="top"
            title="男"
            :getPopupContainer="getPopupContainer"
          >
            <v-icon color="blue" size="25">
              mdi-face
            </v-icon>
          </a-tooltip>

          <a-tooltip
            v-else-if="item.gender === 2"
            placement="top"
            title="女"
            :getPopupContainer="getPopupContainer"
          >
            <v-icon color="pink" size="25">
              mdi-face-woman
            </v-icon>
          </a-tooltip>

          <a-tooltip
            v-else
            placement="top"
            title="未知"
            :getPopupContainer="getPopupContainer"
          >
            <v-icon size="25">
              mdi-account-alert
            </v-icon>
          </a-tooltip>
        </template>

        <template v-slot:item.admin="{ item }">
          <v-chip :color="item.admin ? 'purple' : 'orange'" dark outlined label>
            {{ item.admin ? "管理员" : "雇员" }}
            <v-icon :class="item.admin ? '' : 'ml-6'" right
              >mdi-account-outline
            </v-icon>
          </v-chip>
        </template>

        <template v-slot:item.active="{ item }">
          <v-chip :color="item.active ? 'green' : 'red'" label dark small>
            {{ item.active ? "正常" : "未激活" }}
          </v-chip>
        </template>

        <template v-slot:loading>
          <div
            class="ma-10 d-flex align-center justify-center"
            style="max-height: 200px;min-height: 150px"
          >
            <v-progress-circular
              indeterminate
              :color="showColor"
            ></v-progress-circular>
            <span class="ml-3">正在加载...</span>
          </div>
        </template>
        <template v-slot:no-data>
          <div
            class="ma-10 d-flex-column align-center justify-center"
            style="max-height: 200px;min-height: 150px"
          >
            <v-icon disabled size="40">mdi-dots-horizontal-circle</v-icon>
            <div class="mt-8">没有数据</div>
          </div>
        </template>
      </v-data-table>
    </v-card>
    <div class="text-center" v-if="pageInfo.data.length > 0">
      <v-row justify="end" align="center">
        <v-spacer></v-spacer>
        <v-col class="col-3">
          <v-pagination
            v-model="pageFilter.pageNumber"
            :total-visible="5"
            :length="pageInfo.pages"
            :disabled="loading"
          ></v-pagination>
        </v-col>
        <v-col class="col-3">
          <v-row align="center">
            <v-col class="col-5">
              <v-text-field
                v-model.number="pageFilter.pageSize"
                dense
                prefix="每页"
                hint="每页显示最少大于1条,最多不能超过50条"
                suffix="条"
                class="mt-5"
                type="number"
                clearable
                clear-icon="mdi-close"
                @change="handleInputChange"
              ></v-text-field>
            </v-col>
            <v-col class="col-3">
              <div style="font-size: 16px">总计:{{ pageInfo.total }}</div>
            </v-col>
            <v-spacer></v-spacer>
          </v-row>
        </v-col>
      </v-row>
    </div>
    <employee-edit-dialog
      :data="tempAccount"
      ref="dialog"
      @updateList="handleUpdate"
      :active="active"
    ></employee-edit-dialog>
  </div>
</template>

<script>
import { PAGE, REQUEST, TIME } from "../../../../common/view/Constant";
import StringUtil from "../../../../utils/StringUtil";
import { Modal, Tooltip } from "ant-design-vue";
import { eventBus } from "../../../../bus";
import { EMPLOYEE_TABLE } from "../../../../common/components/Constant";
import { checkPermission } from "../../../../utils/auth/auth";
import { EMPLOYEE_PERM } from "../../../../utils/permission";

const color = ["red", "purple", "green", "primary"];
export default {
  name: "employeeTable",
  props: {
    type: {
      type: String,
      required: true
    },
    active: {
      type: Boolean,
      default: false
    },
    edit: {
      type: Boolean,
      default: true
    },
    remove: {
      type: Boolean,
      default: true
    },
    onlyChose: {
      type: Boolean,
      default: false
    }
  },
  components: {
    aTooltip: Tooltip,
    employeeEditDialog: () => import("../employeeEditDialog.vue")
  },
  data: () => ({
    _timer: null,
    showColor: "amber",
    selected: [],
    searchVal: "",
    loading: true,
    options: {},
    pageInfo: {
      total: 0,
      pages: 0,
      data: []
    },
    pageFilter: {
      pageNumber: 1,
      pageSize: PAGE.PAGE_SIZE,
      data: null,
      sortBy: "",
      sortDesc: false
    },
    headers: [
      { value: "avatarUrl", sortable: false },
      {
        text: "名字",
        align: "left",
        value: "name"
      },
      { text: "Id", value: "id" },
      { text: "性别", sortable: false, value: "gender" },
      { text: "邮箱", value: "email" },
      { text: "电话", value: "phoneNumber" },
      { text: "创建时间", value: "createTime" },
      { text: "角色", value: "admin", align: "center", sortable: false },
      { text: "状态", value: "active", align: "center", sortable: false }
    ],
    tempAccount: null
  }),
  methods: {
    changeColor() {
      let me = this;
      let count = 0;
      this._timer = setInterval(function() {
        if (count === color.length) {
          count = 0;
        }
        me.showColor = color[count++];
        return false;
      }, 500);
    },
    searchValue(val) {
      this.searchVal = val;
    },
    getPopupContainer(trigger) {
      return trigger.parentElement;
    },
    formatTime(list) {
      const me = this;
      if (list) {
        list.forEach(e => {
          e.createTime = me.$moment(e.createTime).format(TIME.DEFAULT_TIME);
          e.updateTime = me.$moment(e.updateTime).format(TIME.DEFAULT_TIME);
        });
      }
      this.pageInfo.data = list;
    },
    addPageFilter() {
      const pageFilter = this.pageFilter;
      const options = this.options;
      pageFilter.sortBy =
        options.sortBy[0] === undefined
          ? "name"
          : StringUtil.toLine(options.sortBy[0]);
      pageFilter.sortDesc =
        options.sortDesc[0] === undefined ? false : options.sortDesc[0];
    },
    changeId() {
      const {
        params: { id }
      } = this.$route;
      if (id) {
        this.pageFilter.data = id;
      }
    },
    async fetchData() {
      this.loading = true;
      try {
        let codeInfo;
        let dataList;
        if (this.type === "project") {
          const {
            data: { code, data }
          } = await this.$api.account.getEmployeeListByProjectId(
            this.pageFilter
          );
          codeInfo = code;
          dataList = data;
        }
        if (this.type === "team") {
          const {
            data: { code, data }
          } = await this.$api.account.getEmployeeListByTeamId(
            this.pageFilter,
            this.active
          );
          codeInfo = code;
          dataList = data;
        }
        if (this.type === "company") {
          const {
            data: { code, data }
          } = await this.$api.account.getEmployeeListByCompanyId(
            this.pageFilter,
            this.active
          );
          codeInfo = code;
          dataList = data;
        }
        if (codeInfo === REQUEST.SUCCESS.code) {
          this.formatTime(dataList.list);
          this.pageInfo.pages = dataList.pages;
          this.pageInfo.total = dataList.total;
        }
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 300);
      }
    },
    handleClickAvartar($event) {
      this.tempAccount = $event;
      this.$refs.dialog.handleOpen();
    },
    handleEditAccount() {
      if (this.selected.some(e => e.id === this.account.id)) {
        this.$notification.error(
          REQUEST.ERROR.code,
          EMPLOYEE_TABLE.NOT_EDIT_YOURSELF_HERE
        );
        return false;
      } else {
        this.tempAccount = this.selected[0];
        this.$refs.dialog.handleEdit();
      }
    },
    showDeleteConfirm() {
      const me = this;
      Modal.confirm({
        title:
          this.selected.length > 1
            ? EMPLOYEE_TABLE.SURE_TO_DELETE_THESE_ACCOUNT
            : EMPLOYEE_TABLE.SURE_TO_DELETE_THIS_ACCOUNT,
        content:
          this.selected.length > 1
            ? EMPLOYEE_TABLE.DELETE_THESE_ACCOUNT_DETAILS
            : EMPLOYEE_TABLE.DELETE_THIS_ACCOUNT_DETAILS,
        okType: "danger",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          me.handleDeleteEmployeeList();
        },
        onCancel() {}
      });
    },
    showRemoveDialog() {
      const me = this;
      Modal.confirm({
        title:
          this.selected.length > 1
            ? EMPLOYEE_TABLE.SURE_TO_REMOVE_THESE_ACCOUNT
            : EMPLOYEE_TABLE.SURE_TO_REMOVE_THIS_ACCOUNT,
        content:
          this.selected.length > 1
            ? EMPLOYEE_TABLE.DELETE_THESE_ACCOUNT_DETAILS
            : EMPLOYEE_TABLE.DELETE_THIS_ACCOUNT_DETAILS,
        okType: "danger",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          me.handleRemove();
        },
        onCancel() {}
      });
    },
    async handleDeleteEmployeeList() {
      if (this.selected.some(e => e.id === this.account.id)) {
        this.$notification.error(
          REQUEST.ERROR.code,
          EMPLOYEE_TABLE.NOT_INCLUDE_YOURSELF
        );
        return false;
      } else {
        const list = this.selected.map(e => e.id);
        let resCode;
        let resMsg;
        if (this.active) {
          const {
            data: { code, msg }
          } = await this.$api.account.deleteEmployee(list);
          resCode = code;
          resMsg = msg;
        } else {
          const {
            data: { code, msg }
          } = await this.$api.account.deleteTempEmployee(list);
          resCode = code;
          resMsg = msg;
        }
        if (REQUEST.SUCCESS.code === resCode) {
          this.selected = [];
          this.fetchData();
          Modal.destroyAll();
          this.$notification.success(resCode, resMsg);
        }
      }
    },
    handleInputChange() {
      if (
        this.pageFilter.pageSize === undefined ||
        this.pageFilter.pageSize < PAGE.PAGE_MIN_VAL ||
        this.pageFilter.pageSize > PAGE.PAGE_MAX_VAL
      )
        this.pageFilter.pageSize = PAGE.PAGE_SIZE;
      this.addPageFilter();
      this.fetchData();
    },
    handleUpdate() {
      this.addPageFilter();
      this.fetchData();
    },
    async handleRemove() {
      let resCode;
      let resMsg;
      const {
        params: { id }
      } = this.$route;
      const list = this.selected.map(e => e.id);
      if (this.type === "team") {
        const {
          data: { code, msg }
        } = await this.$api.team.removeAccountListToTeamMapping(
          id,
          this.active,
          list
        );
        resCode = code;
        resMsg = msg;
      }
      if (this.type === "project") {
        const {
          data: { code, msg }
        } = await this.$api.project.removeAccountListToProjectMapping(id, list);
        resCode = code;
        resMsg = msg;
      }
      if (resCode === REQUEST.SUCCESS.code) {
        this.selected = [];
        this.$notification.success(resCode, resMsg);
        this.fetchData();
      }
    }
  },
  computed: {
    account() {
      return this.$store.getters.getAccount;
    },
    getShowRemove() {
      // eslint-disable-next-line no-useless-escape
      if (/^\/employees\/((?:[^\/]+?))(?:\/(?=$))?$/i.test(this.$route.path)) {
        return !this.onlyChose;
      } else return this.remove;
    },
    showSelect() {
      return (
        checkPermission(EMPLOYEE_PERM.DEL_EMPLOYEE) ||
        checkPermission(EMPLOYEE_PERM.EDIT_EMPLOYEE)
      );
    },
    getEditEmployeePerm() {
      return EMPLOYEE_PERM.EDIT_EMPLOYEE;
    },
    getDelEmployeePerm() {
      return EMPLOYEE_PERM.DEL_EMPLOYEE;
    },
    getRemoveEmployeePerm() {
      return EMPLOYEE_PERM.REMOVE_EMPLOYEE;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  },
  watch: {
    options: {
      handler() {
        this.addPageFilter();
        this.fetchData();
      },
      deep: true
    },
    $route() {
      if (this.type === "team" || this.type === "project") {
        this.changeId();
      }
      this.fetchData();
    }
  },
  created() {
    if (this.type === "team" || this.type === "project") {
      this.changeId();
    } else {
      this.pageFilter.data = this.$store.getters.getCompany.id;
    }
    eventBus.$on("refreshEmployeeData", () => {
      this.addPageFilter();
      this.fetchData();
      this.selected = [];
    });
  },
  mounted() {
    this.changeColor();
  },
  beforeDestroy() {
    window.clearInterval(this._timer);
    eventBus.$off("refreshEmployeeData");
  }
};
</script>

<style scoped>
.employeeTable {
  margin: 0;
  position: relative;
}

h1 {
  font-size: 40px;
  font-weight: lighter;
  color: gray;
}

span {
  font-weight: bolder;
}

span::after {
  display: block;
  content: "";
  height: 10px;
}
</style>
