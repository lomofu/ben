<template>
  <div>
    <user-nav :title="getTitle" :color="dark ? '' : 'teal'">
      <Permission :perm="getAddEmployeePerm">
        <v-btn outlined @click="handleChoose">
          <v-icon class="mr-1">
            mdi-account-plus
          </v-icon>
          添加成员
        </v-btn>
      </Permission>
    </user-nav>
    <div class="content-box">
      <employee-table :type="'team'"></employee-table>
    </div>
    <v-divider></v-divider>
    <div class="content-box">
      <employeeInactiveTable
        :type="'team'"
        ref="employeeTable"
        :key="'teamTable'"
      >
      </employeeInactiveTable>
    </div>
    <choose-member-dialog
      ref="chooseDialog"
      @fuc="handleAddNew"
    ></choose-member-dialog>
    <employee-dialog ref="addNewAccount"></employee-dialog>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { checkStorePermission } from "../utils/auth/auth";
import { EMPLOYEE_PERM } from "../utils/permission";

export default {
  name: "Employee",
  components: {
    userNav: () => import("../components/public/userNav.vue"),
    chooseMemberDialog: () =>
      import("../components/employee/choseMemberDialog.vue"),
    employeeDialog: () =>
      import("../components/public/employee/employeeDialog.vue"),
    employeeTable: () =>
      import("../components/public/employee/table/employeeTable.vue"),
    employeeInactiveTable: () =>
      import("../components/public/employee/table/employeeInactiveTable.vue")
  },
  data: () => ({
    search: ""
  }),
  methods: {
    ...mapActions({
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    handleChoose() {
      this.$refs.chooseDialog.dialog = true;
    },
    handleAddNew() {
      this.$refs.addNewAccount.dialog = true;
      this.$refs.chooseDialog.dialog = false;
    }
  },
  computed: {
    ...mapGetters({
      dark: "getDark"
    }),
    getTitle() {
      const list = this.$store.getters.getSimpleTeamList;
      const val = list.find(e => e.id === this.$route.params.id);
      if (val) {
        return `${val.name} | 团队成员`;
      }
      return "团队成员";
    },
    getAddEmployeePerm() {
      return EMPLOYEE_PERM.ADD_EMPLOYEE;
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
