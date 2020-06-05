<template>
  <div>
    <div v-if="project === null || project === undefined">
      <user-nav title="项目 | 无结果" color="#424242"></user-nav>
      <div class="d-flex justify-center" style="position: relative">
        <a-empty description="没有数据" class="em-box">
          <h4>请确保该项目存在,或尝试刷新加载</h4>
          <v-btn class="mt-5" @click="$router.push('/')" color="purple" outlined
            >返回面板
          </v-btn>
        </a-empty>
      </div>
    </div>

    <div v-else>
      <user-nav :title="getTitle" color="#424242">
        <Permission :perm="getAddEmployeePerm">
          <v-btn outlined class="mr-4" @click="handleAdd">
            <v-icon small> mdi-account-plus</v-icon>
            <span class="ml-1">添加成员</span>
          </v-btn>
        </Permission>
        <Permission :perm="getJobView">
          <v-btn outlined class="mr-4" @click="handleClick">
            <v-icon small>mdi-view-list</v-icon>
            <span class="ml-1">前往排班</span>
          </v-btn>
        </Permission>
      </user-nav>
      <v-row>
        <v-col cols="12" xs="12" sm="12" md="6" lg="6" xl="6">
          <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
            概括
          </v-subheader>
          <circleChart></circleChart>
          <project-chart class="mt-5"></project-chart>
        </v-col>
        <v-col cols="12" xs="12" sm="12" md="6" lg="6" xl="6">
          <project-info-card :data="project"></project-info-card>
        </v-col>
      </v-row>
      <v-divider></v-divider>
      <Permission :perm="getEmployeeViewPerm">
        <div class="table-box ma-3">
          <employee-table
            class="mt-8"
            :type="'project'"
            :key="'projectTable'"
          ></employee-table>
        </div>
      </Permission>
      <Permission :perm="getAddEmployeePerm">
        <addExistEmployeeDialog
          ref="addEmployeeDialog"
          :type="'project'"
        ></addExistEmployeeDialog>
      </Permission>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { Empty } from "ant-design-vue";
import { EMPLOYEE_PERM, JOB_PERM } from "../../../utils/permission";
export default {
  name: "projectNav",
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  components: {
    userNav: () => import("../../public/userNav.vue"),
    projectChart: () => import("./projectChart.vue"),
    projectInfoCard: () => import("./projectInfoCard.vue"),
    aEmpty: Empty,
    employeeTable: () =>
      import("../../public/employee/table/employeeTable.vue"),
    addExistEmployeeDialog: () =>
      import("../../employee/addExistEmployeeDialog.vue"),
    circleChart: () => import("./circleChart.vue")
  },
  data: () => ({
    project: null
  }),
  methods: {
    ...mapMutations({
      updateShowSchedule: "updateShowSchedule"
    }),
    handleClick() {
      this.updateShowSchedule(true);
    },
    handleAdd() {
      this.$refs.addEmployeeDialog.dialog = true;
    }
  },
  computed: {
    ...mapGetters({
      show: "getShowSchedule"
    }),
    getTitle() {
      return `项目 | ${this.project.name}`;
    },
    getAddEmployeePerm() {
      return EMPLOYEE_PERM.ADD_EMPLOYEE;
    },
    getJobView() {
      return JOB_PERM.VIEW_JOB;
    },
    getEmployeeViewPerm() {
      return EMPLOYEE_PERM.VIEW_EMPLOYEE;
    }
  },
  watch: {
    data() {
      this.project = this.data;
    }
  },
  created() {
    this.project = this.data;
  }
};
</script>

<style scoped>
.em-box {
  position: absolute;
  top: 25vh;
}
.table-box {
  min-height: 400px;
  max-height: 500px;
}
</style>
