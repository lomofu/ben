<template>
  <v-dialog
    v-model="dialog"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
    scrollable
  >
    <v-card v-if="formData">
      <div class="mb-12">
        <v-toolbar flat dark :color="dark ? '' : '#00BCD4'">
          <v-btn icon dark @click.stop="dialog = false" :disable="isDisable">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>个人信息</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn
            v-if="canEdit"
            text
            color="red"
            @click="handleRecover"
            :loading="isDisable"
          >
            还原
          </v-btn>
          <v-btn
            v-if="canEdit"
            dark
            text
            @click="handleSave"
            :loading="isDisable"
          >
            保存
          </v-btn>
        </v-toolbar>
      </div>
      <v-card-text class="mt-12">
        <v-row>
          <v-col class="col-6" align="center">
            <v-avatar size="150" style="box-shadow: 0 0 9px 2px #00000038">
              <v-img
                :src="formData.avatarUrl"
                :lazy-src="formData.avatarUrl"
                :alt="formData.name"
              >
                <template v-slot:placeholder>
                  <v-row
                    class="fill-height ma-0"
                    align="center"
                    justify="center"
                  >
                    <v-progress-circular
                      indeterminate
                      color="amber"
                    ></v-progress-circular>
                  </v-row>
                </template>
              </v-img>
            </v-avatar>
            <v-card-subtitle>
              性别:
              {{
                formData.gender === 1
                  ? "男"
                  : formData.gender === 2
                  ? "女"
                  : "未知"
              }}
            </v-card-subtitle>
            <v-card-subtitle>
              角色:
              {{ formData.admin ? "管理员" : formData.role[0] }}
            </v-card-subtitle>
            <v-card-subtitle>
              创建时间:
              {{ $moment(formData.createTime).format("lll") }}
            </v-card-subtitle>
          </v-col>
          <v-col class="col-4">
            <h1 :style="{ color: dark ? 'white' : '' }">账 户</h1>
            <div class="mt-10" style="width: 300px">
              <div>
                <span>ID</span>
                <div class="d-flex justify-start">
                  <v-text-field
                    readonly
                    v-model.trim="formData.id"
                    :outlined="!canEdit"
                    :flat="canEdit"
                    :solo="canEdit"
                    dense
                    class="mr-2"
                  ></v-text-field>
                  <v-btn
                    small
                    icon
                    class="mt-1"
                    v-clipboard:copy="formData.id"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    <v-icon small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
              <div>
                <span>名称</span>
                <div class="d-flex justify-start">
                  <v-text-field
                    :readonly="!canEdit"
                    v-model.trim="formData.name"
                    :error-messages="nameErrors"
                    :counter="canEdit ? 20 : false"
                    outlined
                    dense
                    clear-icon="mdi-close"
                    :clearable="canEdit"
                    :disable="isDisable"
                    @input="handleValidateName"
                    @blur="handleValidateName"
                    class="mr-2"
                  ></v-text-field>
                  <v-btn
                    small
                    icon
                    class="mt-1"
                    v-clipboard:copy="formData.name"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    <v-icon small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
              <div v-if="canEdit">
                <v-radio-group
                  label="性别"
                  v-model="formData.gender"
                  @change="isChangeSex = true"
                  :disable="isDisable"
                  row
                >
                  <v-radio
                    class="mr-3"
                    v-for="n in 3"
                    :key="n"
                    :label="n === 3 ? '保密' : n === 1 ? '男' : '女'"
                    :value="n"
                  ></v-radio>
                </v-radio-group>
              </div>
              <div>
                <span>邮箱</span>
                <div class="d-flex justify-start">
                  <v-text-field
                    :readonly="!canEdit"
                    v-model.trim="formData.email"
                    :error-messages="emailErrors"
                    outlined
                    clear-icon="mdi-close"
                    :clearable="canEdit"
                    @input="handleValidateEmail"
                    @blur="handleValidateEmail"
                    :disable="isDisable"
                    dense
                    class="mr-2"
                  ></v-text-field>
                  <v-btn
                    small
                    icon
                    class="mt-1"
                    v-clipboard:copy="formData.email"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    <v-icon small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
              <div>
                <span>手机</span>
                <div class="d-flex justify-start">
                  <v-text-field
                    :readonly="!canEdit"
                    v-model.trim="formData.phoneNumber"
                    :counter="canEdit ? 11 : false"
                    :error-messages="phoneErrors"
                    outlined
                    dense
                    clear-icon="mdi-close"
                    :clearable="canEdit"
                    @input="handleValidatePhone"
                    @blur="handleValidatePhone"
                    :disable="isDisable"
                    class="mr-2"
                  ></v-text-field>
                  <v-btn
                    small
                    icon
                    class="mt-1"
                    v-clipboard:copy="formData.phoneNumber"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    <v-icon small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
            </div>
          </v-col>
          <v-spacer></v-spacer>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import { validationMixin } from "vuelidate";
import { email, maxLength, required } from "vuelidate/lib/validators";
import { fullName, phone } from "../../../common/validator/validator";
import { EMAIL, FULLNAME, PHONE } from "../../../common/validator/Constant";
import { REQUEST } from "../../../common/view/Constant";
import debounce from "lodash/debounce";
import cloneDeep from "lodash/cloneDeep";
import {
  ACCOUNT_CARD,
  EMPLOYEE_EDIT_DIALOG
} from "../../../common/components/Constant";

export default {
  name: "employeeEditDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      email: {
        required,
        email,
        async isUnique(value) {
          if (
            value === "" ||
            value === null ||
            !/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)
          ) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("email", value);
            return response.data.data;
          }
        }
      },
      name: {
        required,
        illegalChar: fullName.illegalChar(),
        underline: fullName.underline(),
        allNumbers: fullName.allNumbers(),
        maxLength: maxLength(20),
        async isUnique(value) {
          if (
            value === "" ||
            value === null ||
            /^\d*\d\d*$/.test(value) ||
            /(^_)|(__)|(_+$)/.test(value)
          ) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("name", value);
            return response.data.data;
          }
        }
      },
      phoneNumber: {
        required,
        phone: phone,
        async isUnique(value) {
          if (value === "" || value === null || value.length !== 11) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("phone", value);
            return response.data.data;
          }
        }
      }
    }
  },
  props: {
    data: {
      required: true
    },
    active: {
      default: true
    }
  },
  data: () => ({
    dialog: false,
    canEdit: false,
    isDisable: false,
    formData: {}
  }),
  methods: {
    validateHasChange() {
      const temp = this.formData;
      const raw = this.rawFormData;
      return (
        temp.name !== raw.name ||
        temp.phoneNumber !== raw.phoneNumber ||
        temp.email !== raw.email ||
        temp.gender !== raw.gender
      );
    },
    validateNameData() {
      if (this.formData.name !== this.rawFormData.name) {
        const validate = this.$v.formData;
        validate.name.$touch();
        return (
          !validate.name.required ||
          !validate.name.illegalChar ||
          validate.name.underline ||
          validate.name.allNumbers ||
          !validate.name.maxLength ||
          !validate.name.isUnique
        );
      } else {
        return false;
      }
    },
    validatePhoneData() {
      if (this.formData.phoneNumber !== this.rawFormData.phoneNumber) {
        const validate = this.$v.formData;
        validate.phoneNumber.$touch();
        return validate.phoneNumber.$invalid;
      } else {
        return false;
      }
    },
    validateEmailData() {
      if (this.formData.email !== this.rawFormData.email) {
        const validate = this.$v.formData;
        validate.email.$touch();
        return validate.email.$invalid || !validate.email.isUnique;
      } else {
        return false;
      }
    },
    handleValidateName: debounce(function() {
      if (this.canEdit && this.formData.name) this.$v.formData.name.$touch();
    }, 200),
    handleValidateEmail: debounce(function() {
      if (this.canEdit && this.formData.email) this.$v.formData.email.$touch();
    }, 200),
    handleValidatePhone: debounce(function() {
      if (this.canEdit && this.formData.phoneNumber)
        this.$v.formData.phoneNumber.$touch();
    }, 200),
    handleOpen() {
      this.dialog = true;
      this.canEdit = false;
    },
    handleEdit() {
      this.dialog = true;
      this.canEdit = true;
    },
    async handleSave() {
      if (this.validateHasChange()) {
        if (
          this.validateEmailData() ||
          this.validateNameData() ||
          this.validatePhoneData()
        ) {
          return false;
        } else {
          this.isDisable = true;
          try {
            let resCode;
            let resMsg;
            if (this.active) {
              const {
                data: { code, msg }
              } = await this.$api.account.updateAccount(this.formData);
              resCode = code;
              resMsg = msg;
            } else {
              const {
                data: { code, msg }
              } = await this.$api.account.updateEmployee(this.formData);
              resCode = code;
              resMsg = msg;
            }

            if (resCode === REQUEST.SUCCESS.code) {
              this.dialog = false;
              this.$emit("updateList");
              this.$notification.success(resCode, resMsg);
            }
          } finally {
            this.isDisable = false;
          }
        }
      } else {
        this.$notification.error(
          ACCOUNT_CARD.INFO.IS_NOT_CHANGE.TITLE,
          ACCOUNT_CARD.INFO.IS_NOT_CHANGE.CONTENT
        );
      }
    },
    handleRecover() {
      const raw = this.rawFormData;
      this.formData = cloneDeep(raw);
      this.$v.$reset();
    },
    onCopy() {
      this.$notification.success(
        EMPLOYEE_EDIT_DIALOG.CLIPBOARD,
        EMPLOYEE_EDIT_DIALOG.COPY_SUCCESS
      );
    },
    onError() {
      this.$notification.error(
        EMPLOYEE_EDIT_DIALOG.CLIPBOARD,
        EMPLOYEE_EDIT_DIALOG.COPY_FAIL
      );
    },
    validateComputed(type) {
      const validate = this.$v;
      const errors = [];
      if (this.canEdit) {
        switch (type) {
          case "name": {
            if (!validate.formData.name.$dirty) break;
            !validate.formData.name.required &&
              errors.push(FULLNAME.NAME_IS_EMPTY);
            !validate.formData.name.maxLength &&
              errors.push(FULLNAME.NAME_IS_OUT_LIMIT);
            !validate.formData.name.illegalChar &&
              errors.push(FULLNAME.NAME_WITH_ILLEGAL_CHAR);
            validate.formData.name.allNumbers &&
              errors.push(FULLNAME.NAME_WITH_ALL_NUMBERS);
            validate.formData.name.underline &&
              errors.push(FULLNAME.NAME_WITH_UNDERLINE);
            !validate.formData.name.isUnique &&
              errors.push(FULLNAME.NAME_IS_EXIST);
            break;
          }
          case "phone": {
            if (!this.$v.formData.phoneNumber.$dirty) break;
            !validate.formData.phoneNumber.required &&
              errors.push(PHONE.PHONE_IS_EMPTY);
            !validate.formData.phoneNumber.phone &&
              errors.push(PHONE.PHONE_NUMBER_IS_ILLEGAL);
            !validate.formData.phoneNumber.isUnique &&
              errors.push(PHONE.PHONE_IS_EXIST);
            break;
          }
          case "email": {
            if (!validate.formData.email.$dirty) break;
            !validate.formData.email.isUnique &&
              errors.push(EMAIL.EMAIL_IS_EXISTED);
            !validate.formData.email.email && errors.push(EMAIL.EMAIL_ILLEGAL);
            !validate.formData.email.required &&
              errors.push(EMAIL.EMAIL_IS_EMPTY);
            break;
          }
        }
      }
      return errors;
    },
    syncData() {
      this.formData = cloneDeep(this.data);
      this.$store.commit("updateFormData", cloneDeep(this.data));
    }
  },
  computed: {
    nameErrors() {
      return this.validateComputed("name");
    },
    phoneErrors() {
      return this.validateComputed("phone");
    },
    emailErrors() {
      return this.validateComputed("email");
    },
    rawFormData() {
      return this.$store.getters.getFormData;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  },
  watch: {
    data() {
      this.syncData();
    }
  },
  created() {
    this.syncData();
  }
};
</script>

<style scoped></style>
