<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" scrollable persistent no-click-animation>
      <v-card max-height="600px">
        <v-card-title>
          <span class="headline">
            添加雇员
          </span>
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text>
          <employee-table
            :type="'company'"
            :active="true"
            :remove="false"
            :onlyChose="true"
            :edit="false"
            ref="employeeTable"
            :key="'employeeTableForExistEmployee'"
          ></employee-table>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="red"
            @click="dialog = false"
            class="ma-3"
            dark
            :disabled="isDisable"
          >
            关闭
          </v-btn>
          <v-btn
            color="primary"
            @click="handleAdd"
            class="ma-3"
            :disabled="isDisable"
          >
            添加
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { ADD_EXIST_EMPLOYEE_DIALOG } from "../../common/components/Constant";
import { REQUEST } from "../../common/view/Constant";
import { eventBus } from "../../bus";

export default {
  name: "addExistEmployeeDialog",
  props: {
    type: {
      type: String,
      default: function() {
        return "team";
      }
    }
  },
  components: {
    employeeTable: () => import("../public/employee/table/employeeTable.vue")
  },
  data: () => ({
    dialog: false,
    isDisable: false,
    formData: {
      list: []
    }
  }),
  methods: {
    handleData() {
      this.formData.list = this.$refs.employeeTable.selected.map(e => e.id);
      if (this.type === "team") {
        this.formData.teamId = this.$route.params.id;
      } else {
        this.formData.projectId = this.$route.params.id;
      }
    },
    async handleAdd() {
      if (this.$refs.employeeTable.selected.length > 0) {
        this.isDisable = true;
        this.handleData();
        try {
          let resCode;
          let resMsg;
          if (this.type === "team") {
            const {
              data: { code, msg }
            } = await this.$api.team.insertAccountListToTeamMapping(
              this.formData
            );
            resCode = code;
            resMsg = msg;
          } else {
            const {
              data: { code, msg }
            } = await this.$api.project.insertAccountListToProjectMapping(
              this.formData
            );
            resCode = code;
            resMsg = msg;
          }

          if (resCode === REQUEST.SUCCESS.code) {
            this.$refs.employeeTable.selected = [];
            this.$notification.success(resCode, resMsg);
            this.dialog = false;
            eventBus.$emit("refreshEmployeeData");
          }
        } finally {
          this.isDisable = false;
        }
      } else {
        this.$notification.error(
          REQUEST.ERROR.code,
          ADD_EXIST_EMPLOYEE_DIALOG.SELECT_AT_LEAST_ONE
        );
      }
    }
  }
};
</script>

<style scoped></style>
