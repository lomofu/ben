<template>
  <div>
    <v-toolbar
      dense
      class="mt-5"
      max-width="99%"
      flat
      :color="dark ? '#0a0a0a' : ''"
      :dark="dark"
    >
      <v-spacer></v-spacer>
      <Permission :perm="getRoleAdd">
        <v-btn outlined class="ma-3" color="purple" @click="handleAddRole">
          <v-icon class="mr-1">mdi-plus</v-icon>
          添加角色
        </v-btn>
      </Permission>
      <Permission :perm="getRoleEdit">
        <v-btn
          outlined
          class="ma-3"
          :disabled="selected.length !== 1"
          color="primary"
          @click="handleEditRole"
        >
          <v-icon class="mr-1">mdi-pencil-outline</v-icon>
          编辑角色
        </v-btn>
      </Permission>
      <Permission :perm="getRoleDel">
        <v-btn
          outlined
          color="red"
          :disabled="selected.length <= 0"
          @click="showDeleteConfirm"
        >
          <v-icon class="mr-1">mdi-delete-outline</v-icon>
          删除角色
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
        :headers="getTableTitle"
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
        <template v-slot:loading>
          <div
            class="ma-10 d-flex align-center justify-center"
            style="max-height: 200px;min-height: 150px"
          >
            <v-progress-circular indeterminate></v-progress-circular>
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
        <template v-slot:item.op="{ item }">
          <Permission v-if="!checkIsAdmin(item)" :perm="getUpdateRolePerm">
            <v-btn
              color="warning"
              dark
              small
              :elevation="0"
              @click="handleEditPerm(item)"
              >修改权限</v-btn
            >
          </Permission>
        </template>
      </v-data-table>
    </v-card>
    <div class="text-center">
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
                v-model="pageFilter.pageSize"
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
              <span style="font-size: 16px">总计:{{ pageInfo.total }}</span>
            </v-col>
            <v-spacer></v-spacer>
          </v-row>
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { ROLE_PERM } from "../../../utils/permission";
import { PAGE, REQUEST } from "../../../common/view/Constant";
import { checkPermission } from "../../../utils/auth/auth";
import { eventBus } from "../../../bus";
import { Modal } from "ant-design-vue";
import { isEmpty } from "../../../utils/object";

export default {
  name: "roleTable",
  data: () => ({
    searchVal: "",
    loading: true,
    selected: [],
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
    }
  }),
  methods: {
    checkIsAdmin(item) {
      return item.createName === "admin";
    },
    checkIsAdminList() {
      return this.selected.some(e => e.createName === "admin");
    },
    handleAddRole() {
      eventBus.$emit("showAddRoleDialog");
    },
    handleEditRole() {
      if (this.checkIsAdmin(this.selected[0])) {
        this.$notification.error("400", "不可以编辑默认角色");
        return;
      }
      eventBus.$emit("showUpdateRoleDialog", {
        type: "edit",
        data: JSON.parse(JSON.stringify(this.selected[0]))
      });
    },
    showDeleteConfirm() {
      const me = this;
      if (me.checkIsAdminList()) {
        this.$notification.error("400", "不可以删除默认角色");
        return;
      }
      Modal.confirm({
        title:
          this.selected.length > 1
            ? "你确定删除这些角色?"
            : "你确定删除这个项目?",
        okType: "danger",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          me.handleDeleteRole();
        },
        onCancel() {}
      });
    },
    async handleDeleteRole() {
      if (isEmpty(this.selected)) {
        this.$notification.error("你未选中要删除的角色");
      }
      const roleIdList = this.selected.map(e => e.id);
      const {
        data: { code, msg }
      } = await this.$api.role.deleteRoleListById(roleIdList);
      if (REQUEST.SUCCESS.code === code) {
        this.$notification.success(code, msg);
        this.selected = [];
        this.addPageFilter();
        this.fetchData();
        Modal.destroyAll();
      }
    },
    handleEditPerm(item) {
      eventBus.$emit("showPermList", JSON.parse(JSON.stringify(item)));
    },
    addPageFilter() {
      const pageFilter = this.pageFilter;
      const options = this.options;
      pageFilter.sortBy =
        options.sortBy[0] === undefined ? "name" : options.sortBy[0];
      pageFilter.sortDesc =
        options.sortDesc[0] === undefined ? false : options.sortDesc[0];
    },
    async fetchData() {
      this.loading = true;
      try {
        let codeInfo;
        let dataList;
        this.pageFilter.data = this.company.id;
        const {
          data: { code, data }
        } = await this.$api.role.getRoleList(this.pageFilter);
        codeInfo = code;
        dataList = data;
        if (codeInfo === REQUEST.SUCCESS.code) {
          this.pageInfo.pages = dataList.pages;
          this.pageInfo.total = dataList.total;
          this.pageInfo.data = dataList.list;
        }
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 300);
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
    }
  },
  computed: {
    ...mapGetters({
      company: "getCompany"
    }),
    getTableTitle() {
      let headers = [
        { text: "角色名称", value: "name" },
        { text: "描述", value: "description", sortable: false },
        { text: "创建者", value: "createName", sortable: false }
      ];
      if (checkPermission(ROLE_PERM.UPDATE_ROLE_PERM)) {
        headers.push({ text: "操作", value: "op", sortable: false });
      }
      return headers;
    },
    getRoleAdd() {
      return ROLE_PERM.ADD_ROLE;
    },
    getRoleEdit() {
      return ROLE_PERM.EDIT_ROLE;
    },
    getRoleDel() {
      return ROLE_PERM.DEL_ROLE;
    },
    getUpdateRolePerm() {
      return ROLE_PERM.UPDATE_ROLE_PERM;
    },
    showSelect() {
      return (
        checkPermission(ROLE_PERM.DEL_ROLE) ||
        checkPermission(ROLE_PERM.EDIT_ROLE)
      );
    }
  },
  watch: {
    options: {
      handler() {
        this.addPageFilter();
        this.fetchData();
      },
      deep: true
    }
  },
  created() {
    eventBus.$on("refreshTable", () => {
      this.selected = [];
      this.addPageFilter();
      this.fetchData();
    });
  }
};
</script>

<style scoped></style>
