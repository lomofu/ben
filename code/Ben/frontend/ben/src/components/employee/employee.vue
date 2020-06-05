<template>
  <div class="employeePage">
    <v-card>
      <v-card-title class="d-flex justify-center">
        <span class="headline">
          激活账号
        </span>
      </v-card-title>
      <v-avatar
        size="80"
        @click="dialog = true"
        style="cursor: pointer"
        class="hvr-grow-shadow"
      >
        <v-img
          :src="formData.avatarUrl"
          :lazy-src="formData.avatarUrl"
          :alt="formData.name"
        >
          <template v-slot:placeholder>
            <v-row class="fill-height ma-0" align="center" justify="center">
              <v-progress-circular
                indeterminate
                color="amber"
              ></v-progress-circular>
            </v-row>
          </template>
        </v-img>
      </v-avatar>
      <v-card-text>
        <v-container class="mt-3">
          <v-form max-width="25vw" ref="form">
            <v-text-field
              :value="
                formData.teamId ? `TeamId: ${formData.teamId}` : 'TeamId: 暂无'
              "
              label="TeamId:"
              readonly
              flat
              solo
              required
              dense
            ></v-text-field>
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
              @input="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
              @blur="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
              outlined
              dense
            ></v-text-field>
            <v-radio-group label="* 性别: " v-model="formData.gender" row>
              <v-radio
                class="mr-3"
                v-for="n in 3"
                :key="n"
                :label="n === 3 ? '保密' : n === 1 ? '男' : '女'"
                :value="n"
              ></v-radio>
            </v-radio-group>
            <v-divider class="mb-8"></v-divider>
            <v-text-field
              v-model.trim="formData.password"
              :type="showPass1 ? 'text' : 'password'"
              :error-messages="passErrors"
              label="密码:"
              :disabled="isDisable"
              :append-icon="showPass1 ? 'mdi-eye' : 'mdi-eye-off'"
              required
              clear-icon="mdi-close"
              clearable
              @input="formData.password && $v.formData.password.$touch()"
              @blur="formData.password && $v.formData.password.$touch()"
              @click:append="showPass1 = !showPass1"
              outlined
              dense
            ></v-text-field>
            <v-text-field
              v-model.trim="formData.confirmPassword"
              :type="showPass2 ? 'text' : 'password'"
              :error-messages="confirmPassErrors"
              label="确认密码:"
              :disabled="isDisable"
              :append-icon="showPass2 ? 'mdi-eye' : 'mdi-eye-off'"
              required
              clear-icon="mdi-close"
              clearable
              @input="
                formData.confirmPassword && $v.formData.confirmPassword.$touch()
              "
              @blur="
                formData.confirmPassword && $v.formData.confirmPassword.$touch()
              "
              @click:append="showPass2 = !showPass2"
              outlined
              dense
            ></v-text-field>
          </v-form>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="reset" color="red" class="ma-5" dark>还原</v-btn>
        <v-btn @click="handleSubmit" color="primary">激活</v-btn>
      </v-card-actions>
    </v-card>

    <v-dialog v-model="dialog" max-width="400px">
      <v-img
        :src="formData.avatarUrl"
        :lazy-src="formData.avatarUrl"
        :alt="formData.name"
      >
        <template v-slot:placeholder>
          <v-row class="fill-height ma-0" align="center" justify="center">
            <v-progress-circular
              indeterminate
              color="amber"
            ></v-progress-circular>
          </v-row>
        </template>
      </v-img>
    </v-dialog>
  </div>
</template>

<script>
import md5 from "js-md5";
import { validationMixin } from "vuelidate";
import { email, maxLength, required, sameAs } from "vuelidate/lib/validators";
import {
  fullName,
  phone,
  password,
  Email
} from "../../common/validator/validator";
import {
  FULLNAME,
  PHONE,
  PASSWORD,
  EMAIL
} from "../../common/validator/Constant";
import { REQUEST } from "../../common/view/Constant";
import debounce from "lodash/debounce";

export default {
  name: "employee",
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
            const response = await this.$api.register.isUnique("email", value);
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
            const response = await this.$api.register.isUnique("name", value);
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
            const response = await this.$api.register.isUnique("phone", value);
            return response.data.data;
          }
        }
      },
      password: {
        required,
        pass: password
      },
      confirmPassword: {
        required,
        sameAsPassword: sameAs("password")
      }
    }
  },
  data: () => ({
    dialog: false,
    isDisable: false,
    showPass1: false,
    showPass2: false,
    formData: {
      name: "",
      email: "",
      gender: 3,
      avatarUrl: "",
      phoneNumber: "",
      companyId: "",
      teamId: "",
      password: null,
      confirmPassword: null,
      admin: false
    }
  }),
  methods: {
    fetchData() {
      let formData = this.formData;
      const info = this.getEmployeeInfo;
      formData.teamId = info.teamId;
      formData.companyId = info.companyId;
      formData.name = info.name;
      formData.avatarUrl = info.avatarUrl;
      formData.phoneNumber = info.phoneNumber;
      formData.gender = info.gender;
      formData.email = info.email;
    },
    validateFormData() {
      const validate = this.$v.formData;
      validate.$touch();
      return (
        validate.email.$invalid ||
        !validate.email.isUnique ||
        validate.phoneNumber.$invalid ||
        validate.password.$invalid ||
        validate.confirmPassword.$invalid ||
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
    }, 200),
    handleValidateEmail: debounce(function() {
      if (this.formData.email) this.$v.formData.email.$touch();
    }, 200),
    async handleSubmit() {
      const me = this;
      if (this.validateFormData()) {
        return false;
      }
      try {
        let formData = JSON.parse(JSON.stringify(me.formData));
        formData.password = md5(formData.password);
        formData.confirmPassword = md5(formData.confirmPassword);
        me.isDisable = true;
        const {
          data: { code }
        } = await me.$api.employee.postEmployee(formData);
        if (code === REQUEST.SUCCESS.code) {
          me.$store.commit("updateEmployeeDoneView", true);
        }
      } finally {
        me.isDisable = false;
      }
    },
    reset() {
      this.$v.$reset();
      this.fetchData();
    }
  },
  computed: {
    emailErrors() {
      const errors = [];
      if (!this.$v.formData.email.$dirty) return errors;
      !this.$v.formData.email.required && errors.push(EMAIL.EMAIL_IS_EMPTY);
      !this.$v.formData.email.email && errors.push(EMAIL.EMAIL_ILLEGAL);
      !this.$v.formData.email.isUnique && errors.push(EMAIL.EMAIL_IS_EXISTED);
      return errors;
    },
    nameErrors() {
      const errors = [];
      if (!this.$v.formData.name.$dirty) return errors;
      !this.$v.formData.name.required && errors.push(FULLNAME.NAME_IS_EMPTY);
      !this.$v.formData.name.maxLength &&
        errors.push(FULLNAME.NAME_IS_OUT_LIMIT);
      !this.$v.formData.name.illegalChar &&
        errors.push(FULLNAME.NAME_WITH_ILLEGAL_CHAR);
      this.$v.formData.name.allNumbers &&
        errors.push(FULLNAME.NAME_WITH_ALL_NUMBERS);
      this.$v.formData.name.underline &&
        errors.push(FULLNAME.NAME_WITH_UNDERLINE);
      !this.$v.formData.name.isUnique && errors.push(FULLNAME.NAME_IS_EXIST);
      return errors;
    },
    phoneErrors() {
      const errors = [];
      if (!this.$v.formData.phoneNumber.$dirty) return errors;
      !this.$v.formData.phoneNumber.required &&
        errors.push(PHONE.PHONE_IS_EMPTY);
      !this.$v.formData.phoneNumber.phone &&
        errors.push(PHONE.PHONE_NUMBER_IS_ILLEGAL);
      !this.$v.formData.phoneNumber.isUnique &&
        errors.push(PHONE.PHONE_IS_EXIST);
      return errors;
    },
    passErrors() {
      const errors = [];
      if (!this.$v.formData.password.$dirty) return errors;
      !this.$v.formData.password.required &&
        errors.push(PASSWORD.PASSWORD_IS_EMPTY);
      !this.$v.formData.password.pass && errors.push(PASSWORD.PASSWORD_ERROR);
      return errors;
    },
    confirmPassErrors() {
      const errors = [];
      if (!this.$v.formData.confirmPassword.$dirty) return errors;
      !this.$v.formData.confirmPassword.required &&
        errors.push(PASSWORD.PASSWORD_IS_EMPTY);
      !this.$v.formData.confirmPassword.sameAsPassword &&
        errors.push(PASSWORD.CONFIRM_PASS_NOT_SAME);
      return errors;
    },
    getEmployeeInfo() {
      return this.$store.getters.getEmployeeInfo;
    }
  },
  created() {
    this.fetchData();
  }
};
</script>

<style scoped></style>
