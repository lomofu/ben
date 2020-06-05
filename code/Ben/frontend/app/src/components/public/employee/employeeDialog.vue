<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-img
          height="180"
          src="../../../../public/img/employee/employee.png"
          lazy-src="../../../../public/img/employee/employee.png"
        ></v-img>
        <v-card-title>
          <span class="headline">
            添加雇员
          </span>
          <v-list-item-subtitle>
            <small class="grey--text">
              雇员是公司员工的集合,先填写一下雇员的基本信息
            </small>
          </v-list-item-subtitle>
        </v-card-title>
        <v-card-text>
          <v-container class="mt-3">
            <v-form max-width="25vw" ref="form">
              <v-text-field
                v-model.trim="formData.email"
                :error-messages="emailErrors"
                label="* E-mail:"
                clear-icon="mdi-close"
                :disabled="isDisable"
                clearable
                outlined
                required
                dense
                @input="handleValidateEmail"
                @blur="handleValidateEmail"
                maxlength="20"
              ></v-text-field>
              <v-text-field
                v-model.trim="formData.name"
                :error-messages="nameErrors"
                :counter="formData.name === null ? false : 20"
                label="* 用户名: "
                :disabled="isDisable"
                required
                clear-icon="mdi-close"
                clearable
                outlined
                dense
                @input="handleValidateName"
                @blur="handleValidateName"
                maxlength="20"
              ></v-text-field>
              <v-text-field
                v-model.trim="formData.phoneNumber"
                :counter="formData.name === null ? false : 11"
                :error-messages="phoneErrors"
                label="* 手机号码:"
                :disabled="isDisable"
                required
                clear-icon="mdi-close"
                clearable
                @input="
                  formData.phoneNumber && $v.formData.phoneNumber.$touch()
                "
                @blur="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
                outlined
                dense
              ></v-text-field>
              <v-radio-group
                label="* 性别: "
                v-model="formData.gender"
                :disabled="isDisable"
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
              <v-autocomplete
                label="选择雇员所在的团队"
                v-model="teamName"
                :items="teamList"
                auto-select-first
                cache-items
                hide-no-data
                outlined
                required
                @change="handleChange($event)"
                dense
                :disabled="isDisable"
              >
              </v-autocomplete>
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
import { validationMixin } from "vuelidate";
import { email, maxLength, required } from "vuelidate/lib/validators";
import { fullName, phone, Email } from "../../../common/validator/validator";
import { FULLNAME, EMAIL, PHONE } from "../../../common/validator/Constant";
import { REQUEST } from "../../../common/view/Constant";
import debounce from "lodash/debounce";
import { eventBus } from "../../../bus";

export default {
  name: "employeeDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      email: {
        required,
        email,
        async isUnique(value) {
          if (value === "" || value === null || !Email(value)) {
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
  data: () => ({
    isDisable: false,
    dialog: false,
    teamList: [],
    teamName: null,
    formData: {
      name: "",
      email: "",
      gender: 3,
      phoneNumber: "",
      companyId: "",
      teamId: "",
      adminId: ""
    }
  }),
  methods: {
    validateFormData() {
      const validate = this.$v.formData;
      validate.$touch();
      return (
        validate.email.$invalid ||
        !validate.email.isUnique ||
        validate.phoneNumber.$invalid ||
        !validate.name.required ||
        !validate.name.illegalChar ||
        validate.name.underline ||
        validate.name.allNumbers ||
        !validate.name.maxLength ||
        !validate.name.isUnique ||
        this.formData.companyId === "" ||
        this.formData.companyId === null
      );
    },
    handleValidateName: debounce(function() {
      if (this.formData.name) this.$v.formData.name.$touch();
    }, 500),
    handleValidateEmail: debounce(function() {
      if (this.formData.email) this.$v.formData.email.$touch();
    }, 500),
    handleChange($event) {
      this.formData.teamId = this.filterItem($event).id;
    },
    filterItem(val) {
      const list = this.simpleTeamList;
      return list.find(e => e.name === val);
    },
    fetchData() {
      this.teamList = this.$store.getters.getSimpleTeamList.map(e => e.name);
    },
    async handleSubmit() {
      if (this.validateFormData()) {
        return false;
      }
      try {
        this.isDisable = true;
        const {
          data: { code, msg }
        } = await this.$api.account.createTempEmployee(this.formData);
        if (code === REQUEST.SUCCESS.code) {
          this.$notification.success(code, msg);
          this.handleClose();
          eventBus.$emit("refreshEmployeeData");
        }
      } finally {
        this.isDisable = false;
      }
    },
    handleClose() {
      this.$refs.form.reset();
      this.$v.$reset();
      this.dialog = false;
    },
    validateComputed(type) {
      const errors = [];
      switch (type) {
        case "email": {
          if (!this.$v.formData.email.$dirty) break;
          !this.$v.formData.email.required && errors.push(EMAIL.EMAIL_IS_EMPTY);
          !this.$v.formData.email.email && errors.push(EMAIL.EMAIL_ILLEGAL);
          !this.$v.formData.email.isUnique &&
            errors.push(EMAIL.EMAIL_IS_EXISTED);
          break;
        }
        case "name": {
          if (!this.$v.formData.name.$dirty) break;
          !this.$v.formData.name.required &&
            errors.push(FULLNAME.NAME_IS_EMPTY);
          !this.$v.formData.name.maxLength &&
            errors.push(FULLNAME.NAME_IS_OUT_LIMIT);
          !this.$v.formData.name.illegalChar &&
            errors.push(FULLNAME.NAME_WITH_ILLEGAL_CHAR);
          this.$v.formData.name.allNumbers &&
            errors.push(FULLNAME.NAME_WITH_ALL_NUMBERS);
          this.$v.formData.name.underline &&
            errors.push(FULLNAME.NAME_WITH_UNDERLINE);
          !this.$v.formData.name.isUnique &&
            errors.push(FULLNAME.NAME_IS_EXIST);
          break;
        }
        case "phone": {
          if (!this.$v.formData.phoneNumber.$dirty) break;
          !this.$v.formData.phoneNumber.required &&
            errors.push(PHONE.PHONE_IS_EMPTY);
          !this.$v.formData.phoneNumber.phone &&
            errors.push(PHONE.PHONE_NUMBER_IS_ILLEGAL);
          !this.$v.formData.phoneNumber.isUnique &&
            errors.push(PHONE.PHONE_IS_EXIST);
          break;
        }
      }
      return errors;
    }
  },
  computed: {
    emailErrors() {
      return this.validateComputed("email");
    },
    nameErrors() {
      return this.validateComputed("name");
    },
    phoneErrors() {
      return this.validateComputed("phone");
    },
    simpleTeamList() {
      return this.$store.getters.getSimpleTeamList;
    }
  },
  watch: {
    simpleTeamList() {
      this.fetchData();
    }
  },
  created() {
    this.fetchData();
    this.formData.companyId = this.$store.getters.getCompany.id;
    this.formData.adminId = this.$store.getters.getAccount.id;
  }
};
</script>

<style scoped></style>
