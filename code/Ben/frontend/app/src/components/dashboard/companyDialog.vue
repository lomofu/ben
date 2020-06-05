<template>
  <a-drawer
    :title="formData.type ? '更新公司信息' : '更新团队信息'"
    :width="375"
    @close="onClose"
    :visible="visible"
    :drawerStyle="{
      background: dark ? '#141414' : '',
      color: dark ? 'white' : ''
    }"
    :headerStyle="{
      background: dark ? '#474747' : '',
      'border-bottom': 'none'
    }"
  >
    <v-form ref="form">
      Id:
      <v-text-field
        :value="formData.id"
        solo
        flat
        readonly
        dense
      ></v-text-field>
      名称:
      <v-text-field
        v-model.trim="formData.name"
        :counter="formData.name === null ? false : 20"
        :disabled="isDisabled"
        :error-message="nameErrors"
        clear-icon="mdi-close"
        @input="$v.formData.name.$touch()"
        @blur="$v.formData.name.$touch()"
        @change="handlerChange"
        :outlined="!dark"
        :solo="dark"
        dense
        clearable
      ></v-text-field>
      <div v-if="nameErrors.length" class="animated fadeIn">
        <a-alert :message="nameErrors" type="error" banner />
        <br />
      </div>
      地址:
      <v-text-field
        dense
        :counter="formData.name === null ? false : 100"
        v-model.trim="formData.address"
        :outlined="!dark"
        :solo="dark"
        clear-icon="mdi-close"
        clearable
        :disabled="isDisabled"
        @input="$v.formData.address.$touch()"
        @blur="$v.formData.address.$touch()"
        @change="handlerChange"
      ></v-text-field>
      <div v-if="addressErrors.length" class="animated fadeIn">
        <a-alert :message="addressErrors" type="error" banner />
        <br />
      </div>
      描述:
      <v-textarea
        v-model.trim="formData.description"
        :counter="formData.description === null ? false : 100"
        :outlined="!dark"
        :solo="dark"
        dense
        clear-icon="mdi-close"
        clearable
        auto-grow
        :error-message="descriptionErrors"
        :disabled="isDisabled"
        @input="$v.formData.description.$touch()"
        @blur="$v.formData.description.$touch()"
        @change="handlerChange"
      ></v-textarea>
      <div v-if="descriptionErrors.length" class="animated fadeIn">
        <a-alert :message="descriptionErrors" type="error" banner />
        <br />
      </div>
      <v-btn
        text
        @click="handleSave"
        :loading="isDisabled"
        :isDisabled="isDisabled"
      >
        确定
      </v-btn>
      <v-btn
        text
        style="color: red"
        @click="handleRecover"
        :disabled="isDisabled"
      >
        还原
      </v-btn>
    </v-form>
  </a-drawer>
</template>

<script>
import { Alert, Drawer } from "ant-design-vue";
import { eventBus } from "../../bus";
import { mapGetters, mapActions } from "vuex";
import { REQUEST } from "../../common/view/Constant";
import { validationMixin } from "vuelidate";
import { maxLength, required } from "vuelidate/lib/validators";
import { COMPANY_DIALOG } from "../../common/components/Constant";

export default {
  name: "companyDialog",
  mixins: [validationMixin],
  components: {
    aAlert: Alert,
    aDrawer: Drawer
  },
  validations: {
    formData: {
      id: {
        required
      },
      name: {
        required,
        maxLength: maxLength(20)
      },
      address: {
        maxLength: maxLength(100)
      },
      description: {
        maxLength: maxLength(100)
      },
      accountId: {
        required
      }
    }
  },
  data: () => ({
    visible: false,
    isChange: false,
    isDisabled: false,
    formData: {
      id: "",
      name: "",
      description: "",
      address: "",
      accountId: ""
    }
  }),
  methods: {
    onClose() {
      this.visible = false;
    },
    validateData() {
      const validate = this.$v.formData;
      validate.$touch();
      return (
        validate.name.$invalid ||
        validate.description.$invalid ||
        validate.address.$invalid
      );
    },
    async handleSave() {
      this.isDisabled = true;
      this.formData.accountId = this.account.id;
      if (this.isChange) {
        if (this.validateData()) {
          this.isDisabled = false;
          return false;
        } else {
          this.colverNullToString();
          try {
            const {
              data: { code, msg }
            } = await this.$api.company.updateCompany(this.formData);
            if (code === REQUEST.SUCCESS.code) {
              this.fetchCompany(this.formData.id);
              this.$notification.success(code, msg);
              this.onClose();
            }
          } finally {
            this.isDisabled = false;
          }
        }
      } else {
        this.isDisabled = false;
        this.$notification.error(
          COMPANY_DIALOG.INFO.IS_NOT_CHANGE.TITLE,
          COMPANY_DIALOG.INFO.IS_NOT_CHANGE.CONTENT
        );
      }
    },
    colverNullToString() {
      const formData = this.formData;
      if (formData.description === null) formData.description = "";
      if (formData.address === null) formData.address = "";
    },
    handleRecover() {
      this.initialData();
    },
    initialData() {
      this.isChange = false;
      this.formData.id = this.company.id;
      this.formData.name = this.company.name;
      this.formData.description = this.company.description;
      this.formData.address = this.company.address;
    },
    handlerChange() {
      this.isChange = true;
    },
    ...mapActions({
      fetchCompany: "fetchCompany"
    })
  },
  computed: {
    nameErrors() {
      const errors = [];
      if (!this.$v.formData.name.$dirty) return errors;
      !this.$v.formData.name.required &&
        errors.push(COMPANY_DIALOG.NAME.NAME_IS_EMPTY);
      !this.$v.formData.name.maxLength &&
        errors.push(COMPANY_DIALOG.NAME.NAME_IS_OUT_LIMIT);
      return errors;
    },
    descriptionErrors() {
      const errors = [];
      if (!this.$v.formData.description.$dirty) return errors;
      !this.$v.formData.description.maxLength &&
        errors.push(COMPANY_DIALOG.DESCRIPTION.DES_IS_OUT_LIMIT);
      return errors;
    },
    addressErrors() {
      const errors = [];
      if (!this.$v.formData.address.$dirty) return errors;
      !this.$v.formData.address.maxLength &&
        errors.push(COMPANY_DIALOG.ADDRESS.ADDRESS_IS_OUT_LIMIT);
      return errors;
    },
    ...mapGetters({
      company: "getCompany",
      account: "getAccount",
      dark: "getDark"
    })
  },
  created() {
    this.initialData();
    eventBus.$on("showCompanyEditDialog", message => {
      this.handleRecover();
      this.visible = message.index;
    });
  },
  beforeDestroy() {
    eventBus.$off("showCompanyEditDialog");
  }
};
</script>

<style scoped>
div {
  margin: 0;
  position: relative;
}
</style>
