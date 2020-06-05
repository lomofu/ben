<template>
  <div class="registerPage">
    <v-form ref="form" v-if="!getShowDoneView">
      <v-stepper v-model="step" vertical alt-labels class="mr-3">
        <v-stepper-step step="1" editable>
          <span>{{ steps.step1.title }}</span>
        </v-stepper-step>
        <v-stepper-content step="1">
          <div class="ma-5 pa-8">
            <v-text-field
              v-model.trim="formData.email"
              label="E-mail:"
              :height="height"
              read-only
              solo
              flat
              dense
            ></v-text-field>
            <v-text-field
              v-model.trim="formData.name"
              :error-messages="nameErrors"
              :counter="formData.name === null ? false : 20"
              label="用户名: "
              :disabled="isDisable"
              required
              clear-icon="mdi-close"
              clearable
              :height="height"
              outlined
              dense
              @input="handleValidateName"
              @blur="formData.name && $v.formData.name.$touch()"
              maxlength="20"
            ></v-text-field>
            <v-text-field
              v-model.trim="formData.phoneNumber"
              :counter="formData.name === null ? false : 11"
              :error-messages="phoneErrors"
              label="手机号码:"
              :disabled="isDisable"
              required
              clear-icon="mdi-close"
              clearable
              :height="height"
              @input="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
              @blur="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
              outlined
              dense
            ></v-text-field>
            <v-radio-group label="性别: " v-model="formData.gender" row>
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
              :height="height"
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
              :height="height"
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
            <v-btn color="primary" @click="toStep2"
              >{{ steps.step1.btn.word }}
            </v-btn>
            <v-btn text @click="reset1" color="red">重置</v-btn>
          </div>
        </v-stepper-content>

        <v-stepper-step step="2">{{ steps.step2.title }}</v-stepper-step>
        <v-stepper-content step="2" editable>
          <div class="ma-5 pa-5">
            <v-radio-group label="类型: " v-model="formData.type" row>
              <v-radio
                class="mr-3"
                v-for="n in 2"
                :key="n"
                :label="n === 1 ? '公司' : '团队'"
                :value="n"
              ></v-radio>
            </v-radio-group>
            <v-text-field
              v-model.trim="formData.companyName"
              :label="getType"
              :error-messages="companyNameErrors"
              :counter="formData.name === null ? false : 20"
              :disabled="isDisable"
              required
              clear-icon="mdi-close"
              clearable
              :height="height"
              @input="formData.companyName && $v.formData.companyName.$touch()"
              @blur="formData.companyName && $v.formData.companyName.$touch()"
              outlined
              dense
              maxlength="20"
            ></v-text-field>
            <v-text-field
              v-model.trim="formData.companyAddress"
              :label="getAddress"
              :error-messages="companyAddressErrors"
              :counter="formData.name === null ? false : 100"
              :disabled="isDisable"
              required
              clear-icon="mdi-close"
              clearable
              :height="height"
              @input="
                formData.companyAddress && $v.formData.companyAddress.$touch()
              "
              @blur="
                formData.companyAddress && $v.formData.companyAddress.$touch()
              "
              outlined
              dense
              maxlength="100"
            ></v-text-field>
            <v-btn color="primary" @click="step = 1" text :disabled="isDisable"
              >{{ steps.step2.btns[0].word }}
            </v-btn>
            <v-btn
              color="primary"
              @click="handleSubmit"
              :disabled="isDisable"
              :loading="isDisable"
              >{{ steps.step2.btns[1].word }}
            </v-btn>
            <v-btn text @click="reset2" color="red" :disabled="isDisable"
              >重置
            </v-btn>
          </div>
        </v-stepper-content>
      </v-stepper>
    </v-form>
  </div>
</template>

<script>
import md5 from "js-md5";
import { validationMixin } from "vuelidate";
import { email, maxLength, required, sameAs } from "vuelidate/lib/validators";
import { fullName, phone, password } from "../../common/validator/validator";
import {
  FULLNAME,
  PHONE,
  PASSWORD,
  COMPANY
} from "../../common/validator/Constant";
import { REQUEST } from "../../common/view/Constant";
import debounce from "lodash/debounce";

export default {
  name: "register",
  mixins: [validationMixin],
  validations: {
    formData: {
      email: {
        required,
        email
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
      },
      companyName: {
        required,
        maxLength: maxLength(20)
      },
      companyAddress: {
        maxLength: maxLength(100)
      }
    }
  },
  props: ["height", "btnHeight"],
  data: () => ({
    formData: {
      email: null,
      name: null,
      gender: 3,
      phoneNumber: null,
      password: null,
      confirmPassword: null,
      type: 1,
      companyName: null,
      companyAddress: null
    },
    isDisable: false,
    showPass1: false,
    showPass2: false,
    step: 1,
    steps: {
      step1: {
        title: "关于你的信息",
        inputs: [
          {
            icon: "mdi-at"
          },
          {
            icon: "mdi-at"
          },
          {}
        ],
        btn: {
          icon: "mdi-at",
          word: "下 一 步"
        }
      },
      step2: {
        title: "关于你的公司/团队 ",
        inputs: [
          {
            icon: "mdi-at"
          },
          {
            icon: "mdi-at"
          },
          {}
        ],
        btns: [
          {
            icon: "mdi-at",
            word: "上 一 步"
          },
          {
            icon: "mdi-at",
            word: "提 交"
          }
        ]
      }
    }
  }),
  methods: {
    validateFormData() {
      const validate = this.$v.formData;
      validate.$touch();
      return (
        validate.email.$invalid ||
        validate.phoneNumber.$invalid ||
        validate.password.$invalid ||
        validate.confirmPassword.$invalid ||
        !validate.name.required ||
        !validate.name.illegalChar ||
        validate.name.underline ||
        validate.name.allNumbers ||
        !validate.name.maxLength ||
        !validate.name.isUnique
      );
    },
    handleValidateName: debounce(function() {
      if (this.formData.name) this.$v.formData.name.$touch();
    }, 500),
    toStep2() {
      if (this.validateFormData()) {
        return false;
      }
      this.step = 2;
    },
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
        } = await me.$api.register.register(formData);
        if (code === REQUEST.SUCCESS.code) {
          me.$store.dispatch("asyncUpdateRegisterDoneView", true);
        }
      } finally {
        me.isDisable = false;
      }
    },
    reset1() {
      this.isDisable = false;
      const formData = this.formData;
      formData.name = null;
      formData.phoneNumber = null;
      formData.password = null;
      formData.confirmPassword = null;
      formData.gender = 3;
      this.$v.$reset();
    },
    reset2() {
      this.isDisable = false;
      const formData = this.formData;
      formData.companyName = null;
      formData.companyAddress = null;
      this.$v.$reset();
    },
    reset() {
      this.isDisable = false;
      const formData = this.formData;
      formData.name = null;
      formData.phoneNumber = null;
      formData.password = null;
      formData.confirmPassword = null;
      formData.gender = 3;
      formData.companyName = null;
      formData.companyAddress = null;
      this.$v.$reset();
    }
  },
  computed: {
    getRegisterEmail() {
      return this.$store.getters.getEmail;
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
    companyNameErrors() {
      const errors = [];
      if (!this.$v.formData.companyName.$dirty) return errors;
      if (!this.$v.formData.companyName.required) {
        if (this.formData.type === 1) {
          errors.push(COMPANY.COMPANY_NAME_IS_EMPTY);
        } else {
          errors.push(COMPANY.TEAM_NAME_IS_EMPTY);
        }
      }
      if (!this.$v.formData.companyName.maxLength) {
        if (this.formData.type === 1) {
          errors.push(COMPANY.COMPANY_NAME_IS_OUT_OF_LIMIT);
        } else {
          errors.push(COMPANY.TEAM_NAME_IS_OUT_OF_LIMIT);
        }
      }
      return errors;
    },
    companyAddressErrors() {
      const errors = [];
      if (!this.$v.formData.companyAddress.$dirty) return errors;
      if (!this.$v.formData.companyAddress.maxLength) {
        if (this.formData.type === 1) {
          errors.push(COMPANY.COMPANY_ADDRESS_IS_OUT_OF_LIMIT);
        } else {
          errors.push(COMPANY.TEAM_ADDRESS_IS_OUT_OF_LIMIT);
        }
      }
      return errors;
    },
    getType() {
      return this.formData.type === 1 ? "公司名称:" : "团队名称:";
    },
    getAddress() {
      return this.formData.type === 1 ? "公司地址:" : "团队位置:";
    },
    getShowDoneView() {
      return this.$store.getters.getRegisterDoneView;
    }
  },
  created() {
    this.formData.email = this.getRegisterEmail;
  }
};
</script>

<style lang="scss" scoped>
.registerPage {
  .set-email {
    &::after {
      @include before-after(30px);
    }
  }

  .btn-word {
    @include btn-color;
    @include font-bold;
    font-size: 22px;
  }
}

.v-stepper__label {
  margin-right: 10px !important;
}

.theme--light.v-stepper--vertical .v-stepper__content:not(:last-child) {
  border-left: 0;
}

.v-stepper--vertical .v-stepper__content {
  margin: 0 auto;
  padding: 0;
}

.v-application--is-ltr .v-stepper--vertical .v-stepper__step__step {
  margin-right: 0;
}
</style>
