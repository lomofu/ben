<template>
  <div>
    <v-toolbar
      dense
      class="ma-2"
      max-width="99%"
      flat
      :color="dark ? '#0a0a0a' : ''"
      :dark="dark"
    >
      <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
        列表
      </v-subheader>
      <v-spacer></v-spacer>
      <search
        @updateSearch="updateSearch"
        @cancel="cancel"
        @searchValue="searchValue"
        :filter="pageFilter"
      ></search>
      <Permission :perm="getProjectEdit">
        <v-btn
          outlined
          class="ma-3"
          color="primary"
          :disabled="selected.length !== 1"
          @click="handleEditProject"
        >
          <v-icon class="mr-1">mdi-pencil-outline</v-icon>
          编辑
        </v-btn>
      </Permission>
      <Permission :perm="getProjectDel">
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
        <template v-slot:item.action="{ item }">
          <v-btn
            icon
            @click="
              $router.push({
                path: `/projects/${item.id}`
              })
            "
          >
            <v-icon>
              mdi-information-variant
            </v-icon>
          </v-btn>
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
import { PAGE, REQUEST, TIME } from "../../common/view/Constant";
import { Modal } from "ant-design-vue";
import { mapGetters, mapActions } from "vuex";
import { eventBus } from "../../bus";
import { checkPermission } from "../../utils/auth/auth";
import { PROJECT_PERM } from "../../utils/permission";

const color = ["red", "purple", "green", "primary"];
export default {
  name: "projectTable",
  components: {
    search: () => import("../../components/projectlist/searchForProject.vue")
  },
  data: () => ({
    _timer: null,
    showColor: "amber",
    searchVal: "",
    selected: [],
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
      { text: "项目名称", value: "name" },
      {
        text: "Id",
        align: "left",
        value: "id"
      },
      { text: "创建时间", value: "createTime" },
      { text: "更新时间", value: "updateTime" },
      { text: "团队名称", value: "teamName" },
      { text: "详情", value: "action", sortable: false }
    ]
  }),
  methods: {
    ...mapActions({
      fetchMenuData: "fetchMenuData"
    }),
    changeColor() {
      let me = this;
      this.version = this.$store.getters.getVersion;
      let count = 0;
      this._timer = setInterval(function() {
        if (count === color.length) {
          count = 0;
        }
        me.showColor = color[count++];
        return false;
      }, 600);
    },
    formatTime(list) {
      const me = this;
      list.forEach(e => {
        e.createTime = me.$moment(e.createTime).format(TIME.DEFAULT_TIME);
        e.updateTime = me.$moment(e.updateTime).format(TIME.DEFAULT_TIME);
      });
      this.pageInfo.data = list;
    },
    addPageFilter() {
      const pageFilter = this.pageFilter;
      const options = this.options;
      pageFilter.sortBy =
        options.sortBy[0] === undefined ? "createTime" : options.sortBy[0];
      pageFilter.sortDesc =
        options.sortDesc[0] === undefined ? true : options.sortDesc[0];
    },
    async fetchData() {
      this.loading = true;
      try {
        let codeInfo;
        let dataList;
        this.pageFilter.data = this.company.id;
        if (this.getProjectSearchAll) {
          const {
            data: { code, data }
          } = await this.$api.project.getProjectListByAccountId(
            this.pageFilter
          );
          codeInfo = code;
          dataList = data;
        } else {
          const {
            data: { code, data }
          } = await this.$api.search.company.searchForProject(
            this.pageFilter,
            this.searchVal
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
    handleEditProject() {
      const data = this.selected[0];
      eventBus.$emit("showProjectDialog", {
        type: "edit",
        index: true,
        teamId: data.teamId,
        data: {
          id: data.id,
          name: data.name,
          description: data.description,
          createTime: data.createTime,
          updateTime: data.updateTime
        }
      });
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
    showDeleteConfirm() {
      const me = this;
      Modal.confirm({
        title:
          this.selected.length > 1
            ? "你确定删除这些项目?"
            : "你确定删除这个项目?",
        content:
          this.selected.length > 1
            ? "这将删除选择的这个项目下的所有任务和班次以及其他重要信息"
            : "这将删除选择的这些团队下的所有任务和班次以及其他重要信息",
        okType: "danger",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          me.handleDeleteProjectList();
        },
        onCancel() {}
      });
    },
    async handleDeleteProjectList() {
      const list = this.selected.map(e => e.id);
      const {
        data: { code, msg }
      } = await this.$api.project.deleteProjectList(list);
      if (REQUEST.SUCCESS.code === code) {
        this.selected = [];
        this.fetchData();
        this.fetchMenuData(this.account.id);
        Modal.destroyAll();
        this.$notification.success(code, msg);
      }
    },
    searchValue(val) {
      this.searchVal = val;
    },
    updateSearch(val) {
      this.selected = [];
      this.pageInfo.pages = val.pages;
      this.pageInfo.total = val.total;
      this.formatTime(val.list);
    },
    cancel() {
      this.$store.commit("updateProjectSearchAll", true);
      this.pageFilter.data = this.company.id;
      this.searchVal = null;
      this.selected = [];
      this.addPageFilter();
      this.fetchData();
    }
  },
  computed: {
    ...mapGetters({
      company: "getCompany",
      getProjectSearchAll: "getProjectSearchAll",
      dark: "getDark"
    }),
    showSelect() {
      return (
        checkPermission(PROJECT_PERM.DEL_PROJECT) ||
        checkPermission(PROJECT_PERM.EDIT_PROJECT)
      );
    },
    getProjectEdit() {
      return PROJECT_PERM.EDIT_PROJECT;
    },
    getProjectDel() {
      return PROJECT_PERM.DEL_PROJECT;
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
    eventBus.$on("fetchProjectList", () => this.fetchData());
  },
  mounted() {
    this.changeColor();
  },
  beforeDestroy() {
    window.clearInterval(this._timer);
    eventBus.$off("fetchProjectList");
    this.cancel();
  }
};
</script>

<style scoped></style>
