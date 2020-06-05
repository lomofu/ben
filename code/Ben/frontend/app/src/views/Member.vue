<template>
  <div>
    <user-nav title="雇员列表" :color="dark ? '' : 'orange'">
      <Permission :perm="getAddEmployeePerm">
        <v-btn outlined @click="handleAddEmployee">
          <v-icon class="mr-1">
            mdi-account-plus
          </v-icon>
          添加雇员
        </v-btn>
      </Permission>
    </user-nav>
    <div class="content-box">
      <Permission :perm="getViewEmployeePerm">
        <employee-table
          :type="'company'"
          :remove="false"
          :key="'memberTable'"
        ></employee-table>
      </Permission>
    </div>

    <v-divider></v-divider>
    <div class="content-box">
      <Permission :perm="getViewEmployeePerm">
        <employeeInactiveTable
          :remove="false"
          :type="'company'"
        ></employeeInactiveTable>
      </Permission>
    </div>
    <Permission :perm="getAddEmployeePerm">
      <employee-dialog ref="memberDialog"></employee-dialog>
    </Permission>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import { checkStorePermission } from "../utils/auth/auth";
import { EMPLOYEE_PERM } from "../utils/permission";

export default {
  name: "Member",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    employeeDialog: () =>
      import("../components/public/employee/employeeDialog.vue"),
    employeeTable: () =>
      import("../components/public/employee/table/employeeTable.vue"),
    employeeInactiveTable: () =>
      import("../components/public/employee/table/employeeInactiveTable.vue")
  },
  methods: {
    ...mapActions({
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    handleAddEmployee() {
      this.$refs.memberDialog.dialog = true;
    }
  },
  computed: {
    dark() {
      return this.$store.getters.getDark;
    },
    getAddEmployeePerm() {
      return EMPLOYEE_PERM.ADD_EMPLOYEE;
    },
    getViewEmployeePerm() {
      return EMPLOYEE_PERM.VIEW_EMPLOYEE;
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

<style scoped>
.content-box {
  height: 50vh;
  overflow-x: hidden;
  overflow-y: scroll;
}
</style>
