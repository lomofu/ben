<template>
  <div>
    <v-row justify="center">
      <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
        <v-card>
          <v-img
            height="200"
            src="../../../../public/img/role/role.png"
            lazy-src="../../../../public/img/role/role.png"
          ></v-img>
          <v-card-title>
            <span class="headline">
              {{ type === "add" ? "添加角色" : "修改角色" }}
            </span>
            <v-list-item-subtitle>
              <small class="grey--text">
                角色用于管理权限,而角色会与账户绑定从而控制权限
              </small>
            </v-list-item-subtitle>
          </v-card-title>
          <v-card-text>
            <v-container class="mt-3">
              <v-form max-width="25vw" ref="form">
                <v-text-field
                  label="* 角色名称"
                  v-model.trim="formData.name"
                  :counter="formData.name === null ? false : 20"
                  :error-messages="nameErrors"
                  @input="$v.formData.name.$touch()"
                  @blur="$v.formData.name.$touch()"
                  clear-icon="mdi-close"
                  clearable
                  outlined
                  required
                  :disabled="isDisable"
                >
                </v-text-field>
                <v-textarea
                  label="团队描述"
                  v-model.trim="formData.description"
                  :counter="formData.description === null ? false : 100"
                  :error-message="descriptionErrors"
                  @input="$v.formData.description.$touch()"
                  @blur="$v.formData.description.$touch()"
                  :background-color="dark ? '' : 'yellow lighten-5'"
                  clear-icon="mdi-close"
                  clearable
                  outlined
                  auto-grow
                  :disabled="isDisable"
                >
                </v-textarea>
              </v-form>
            </v-container>
            <small class="red--text">* 表示内容必填</small>
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
              v-if="type === 'add'"
              color="blue darken-1"
              text
              @click="handleSubmit"
              :loading="isDisable"
            >
              创建角色
            </v-btn>
            <v-btn
              v-if="type === 'edit'"
              color="blue darken-1"
              text
              @click="handleUpdate"
              :loading="isDisable"
            >
              修改角色
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import { eventBus } from "../../../bus";
import { validationMixin } from "vuelidate";
import { maxLength, required } from "vuelidate/lib/validators";
import { ROLE_DIALOG } from "../../../common/components/Constant";
import { isEmpty } from "../../../utils/object";
import { REQUEST } from "../../../common/view/Constant";

export default {
  name: "roleAddDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      name: {
        required,
        maxLength: maxLength(20)
      },
      description: {
        maxLength: maxLength(100)
      }
    }
  },
  data: () => ({
    isDisable: false,
    type: "add",
    dialog: false,
    formData: {
      id: null,
      name: null,
      description: null,
      createBy: null
    }
  }),
  methods: {
    validateData() {
      const validate = this.$v.formData;
      validate.$touch();
      return validate.name.$invalid || validate.description.$invalid;
    },
    handleClose() {
      this.dialog = false;
      this.type = "add";
      this.formData.id = null;
      this.formData.name = null;
      this.formData.description = null;
      this.formData.createBy = null;
      this.$refs.form.reset();
      this.$v.$reset();
    },
    async handleSubmit() {
      this.isDisable = true;
      if (this.validateData()) {
        this.isDisable = false;
        return false;
      } else {
        if (isEmpty(this.formData.createBy)) {
          this.formData.createBy = this.$store.getters.getAccount.id;
        }
        if (isEmpty(this.formData.companyId)) {
          this.formData.companyId = this.$store.getters.getCompany.id;
        }
        try {
          const {
            data: { code, msg }
          } = await this.$api.role.createNewRole(this.formData);
          if (REQUEST.SUCCESS.code === code) {
            this.$notification.success(code, msg);
            eventBus.$emit("refreshTable");
            this.handleClose();
          }
        } finally {
          this.isDisable = false;
        }
      }
    },
    async handleUpdate() {
      this.isDisable = true;
      if (this.isNotChange()) {
        this.$notification.warn(
          ROLE_DIALOG.NO_CHANGE.TITLE,
          ROLE_DIALOG.NO_CHANGE.CONTENT
        );
        this.isDisable = false;
        return false;
      }

      if (isEmpty(this.formData.companyId)) {
        this.formData.companyId = this.$store.getters.getCompany.id;
      }

      try {
        const {
          data: { code, msg }
        } = await this.$api.role.updateRole(this.formData);
        if (REQUEST.SUCCESS.code === code) {
          this.$notification.success(code, msg);
          eventBus.$emit("refreshTable");
          this.handleClose();
        }
      } finally {
        this.isDisable = false;
      }
    },
    isNotChange() {
      const raw = this.role;
      const formData = this.formData;
      return (
        raw.name === formData.name && raw.description === formData.description
      );
    },
    validateComputed(type) {
      const errors = [];
      switch (type) {
        case "name": {
          if (!this.$v.formData.name.$dirty) return errors;
          !this.$v.formData.name.required &&
            errors.push(ROLE_DIALOG.NAME.NAME_IS_EMPTY);
          !this.$v.formData.name.maxLength &&
            errors.push(ROLE_DIALOG.NAME.NAME_IS_OUT_LIMIT);
          break;
        }
        case "description": {
          if (!this.$v.formData.description.$dirty) return errors;
          !this.$v.formData.description.maxLength &&
            errors.push(ROLE_DIALOG.DESCRIPTION.DES_IS_OUT_LIMIT);
          break;
        }
      }
      return errors;
    },
    coverNullToString() {
      const formData = this.formData;
      if (formData.description === null) formData.description = "";
    }
  },
  computed: {
    nameErrors() {
      return this.validateComputed("name");
    },
    descriptionErrors() {
      return this.validateComputed("description");
    },
    role() {
      return this.$store.getters.getRole;
    }
  },
  created() {
    this.formData.createBy = this.$store.getters.getAccount.id;
    this.formData.companyId = this.$store.getters.getCompany.id;
    eventBus.$on("showAddRoleDialog", () => {
      this.dialog = true;
    });
    eventBus.$on("showUpdateRoleDialog", message => {
      this.dialog = true;
      this.type = message.type;
      this.$store.commit(
        "updateRole",
        JSON.parse(JSON.stringify(message.data))
      );
      this.formData = JSON.parse(JSON.stringify(message.data));
    });
  },
  beforeDestroy() {
    eventBus.$off("showAddRoleDialog");
    eventBus.$off("showUpdateRoleDialog");
  }
};
</script>

<style scoped></style>
