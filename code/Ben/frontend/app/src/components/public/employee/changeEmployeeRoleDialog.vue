<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-card-title>
          <span class="headline">
            修改员工角色
          </span>
          <v-list-item-subtitle>
            <small class="grey--text">
              每一个角色对应着一部分权限集
            </small>
          </v-list-item-subtitle>
        </v-card-title>
        <v-card-text>
          <v-card :color="dark ? 'orange darken-5' : '#FFF59D'" outlined>
            <v-card-title>
              源角色:
            </v-card-title>
            <v-card-title>
              {{ getRole }}
            </v-card-title>
          </v-card>
          <p class="text-center ma-5">
            <v-icon size="50" color="green">mdi-arrow-down-bold</v-icon>
          </p>
          <v-card outlined>
            <v-card-title>
              目标角色:
            </v-card-title>
            <v-card-text>
              <v-autocomplete
                label="选择角色"
                v-model="formData.roleName"
                :items="roleList"
                :error-messages="selectError"
                :placeholder="roleList.length <= 0 && '当前无角色可选'"
                auto-select-first
                cache-items
                hide-no-data
                outlined
                required
                @change="handleChange($event)"
                dense
              >
              </v-autocomplete>
            </v-card-text>
          </v-card>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="red darken-1"
            text
            @click="handleClose"
            :disabled="isDisable"
            >关闭
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="handleSubmit"
            :loading="isDisable"
          >
            创建
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { eventBus } from "../../../bus";
import { required } from "vuelidate/lib/validators";
import { validationMixin } from "vuelidate";
import { mapActions, mapGetters } from "vuex";
import { REQUEST } from "../../../common/view/Constant";

export default {
  name: "changeEmployeeRoleDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      accountId: {
        required
      },
      roleName: {
        required
      }
    }
  },
  data: () => ({
    dialog: false,
    isDisable: false,
    raw: null,
    formData: {
      accountId: "",
      roleId: "",
      roleName: ""
    }
  }),
  methods: {
    ...mapActions({
      fetchSimpleRoleList: "fetchSimpleRoleList"
    }),
    filterItem(val) {
      const list = this.simpleRoleList;
      return list.find(e => e.name === val);
    },
    handleChange($event) {
      this.formData.roleId = this.filterItem($event).id;
    },
    handleClose() {
      this.isDisable = false;
      this.formData.accountId = null;
      this.formData.roleId = null;
      this.formData.roleName = null;
      this.dialog = false;
    },
    async handleSubmit() {
      this.isDisable = true;
      if (this.raw.role[0] === this.formData.roleName) {
        this.isDisable = false;
        this.$notification.warn("Ops", "你似乎没有改变角色");
        return;
      }
      try {
        const {
          data: { code, msg }
        } = await this.$api.role.updateAccountRole(this.formData);
        if (REQUEST.SUCCESS.code === code) {
          this.$notification.success(code, msg);
          this.handleClose();
          eventBus.$emit("refreshEmployeeData");
        }
      } finally {
        this.isDisable = false;
      }
    }
  },
  computed: {
    ...mapGetters({
      dark: "getDark",
      simpleRoleList: "getSimpleRoleList"
    }),
    roleList() {
      return this.simpleRoleList.map(e => e.name);
    },
    selectError() {
      return [];
    },
    getRole() {
      if (this.raw) return this.raw.role[0];
      return null;
    }
  },
  created() {
    this.fetchSimpleRoleList();
    eventBus.$on("showChangeRole", data => {
      this.dialog = true;
      this.raw = data;
      this.formData.accountId = data.id;
    });
  },
  beforeDestroy() {
    eventBus.$off("showChangeRole");
  }
};
</script>

<style scoped></style>
