<template>
  <v-form style="min-width: 350px;max-width: 450px;" class="login-panel">
    <v-text-field
      label="请输入手机号码"
      v-model.trim="formData.phoneNumber"
      required
      outlined
      clearable
      dense
      clear-icon="mdi-close"
      :disabled="isDisable"
      @input="$v.formData.phoneNumber.$touch()"
      @blur="formData.phoneNumber && $v.formData.phoneNumber.$touch()"
      :error-messages="phoneNumberErrors"
    ></v-text-field>
    <div class="d-flex justify-start">
      <v-text-field
        label="请输入验证码"
        class="mr-2"
        v-model.trim="formData.code"
        style="max-width: 360px"
        required
        outlined
        clearable
        dense
        clear-icon="mdi-close"
        :disabled="isDisable"
        @input="$v.formData.code.$touch()"
        @blur="formData.code && $v.formData.code.$touch()"
        :error-messages="codeErrors"
      ></v-text-field>
      <v-btn
        class="ml-12"
        color="primary"
        @click="handleSendCode"
        :loading="send"
        :disabled="isSend"
      >
        <span v-if="!isSend">获取验证码</span>
        <span v-if="isSend">{{ count }}秒后重发</span>
      </v-btn>
    </div>
    <div class="d-flex justify-space-between" style="width: 100%">
      <v-switch
        v-model="formData.isRememberMe"
        :label="formData.isRememberMe ? '记住我' : '不要记住我'"
      ></v-switch>
      <v-btn
        text
        class="mt-3"
        color="primary"
        @click="$store.commit('updateIsPassword', true)"
      >
        <span>账户密码登录</span>
      </v-btn>
    </div>
    <span class="btn-login mt-3">
      <v-btn
        block
        color="deep-purple lighten-1"
        dark
        height="50px"
        @click="handleLogin"
        style="font-weight: bold;font-size: 20px"
        :loading="isDisable"
        :disabled="isDisable"
      >
        登 录
      </v-btn>
    </span>
  </v-form>
</template>

<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";
import { code, phone } from "../../common/validator/validator";
import { CODE, PHONE } from "../../common/validator/Constant";
import { REQUEST } from "../../common/view/Constant";

export default {
  name: "loginWithCode",
  mixins: [validationMixin],
  validations: {
    formData: {
      phoneNumber: {
        required,
        phone: phone
      },
      code: {
        required,
        code: code
      }
    }
  },
  data: () => ({
    isDisable: false,
    isSend: false,
    send: false,
    count: 60,
    _timer: null,
    formData: {
      phoneNumber: null,
      code: null,
      isRememberMe: false
    }
  }),
  methods: {
    validateError(type) {
      const errors = [];
      switch (type) {
        case "phone": {
          if (!this.$v.formData.phoneNumber.$dirty) break;
          !this.$v.formData.phoneNumber.required &&
            errors.push(PHONE.PHONE_IS_EMPTY);
          !this.$v.formData.phoneNumber.phone &&
            errors.push(PHONE.PHONE_NUMBER_IS_ILLEGAL);
          break;
        }
        case "code": {
          if (!this.$v.formData.code.$dirty) break;
          !this.$v.formData.code.required && errors.push(CODE.CODE_IS_EMPTY);
          !this.$v.formData.code.code && errors.push(CODE.CODE_IS_ILLEGAL);
          break;
        }
      }
      return errors;
    },
    validateData() {
      const validate = this.$v.formData;
      validate.$touch();
      return validate.phoneNumber.$invalid || validate.code.$invalid;
    },
    async handleSendCode() {
      const phoneNumber = this.$v.formData.phoneNumber;
      phoneNumber.$touch();
      if (phoneNumber.$invalid) {
        return false;
      } else {
        this.send = true;
        const {
          data: { code, msg }
        } = await this.$api.login.getCode(this.formData.phoneNumber);
        if (REQUEST.SUCCESS.code === code) {
          this.$msg.success(msg);
          this.isSend = true;
          this.timer();
        }
        this.send = false;
      }
    },
    async handleLogin() {
      if (this.validateData()) {
        return false;
      } else {
        const {
          data: { code, msg, data }
        } = await this.$api.login.loginWithCode(this.formData);
        if (REQUEST.SUCCESS.code === code) {
          this.$msg.success(msg);
          this.$cookies.set("ben-user", data);
          setTimeout(() => {
            window.location.replace(process.env.VUE_APP_CENTER);
          }, 700);
        }
      }
    },
    timer() {
      let time = setInterval(() => {
        if (this.count > 0) {
          this.count--;
        } else {
          clearInterval(time);
          this.isSend = false;
          this.count = 60;
        }
      }, 1000);
      this._timer = time;
    }
  },
  computed: {
    phoneNumberErrors() {
      return this.validateError("phone");
    },
    codeErrors() {
      return this.validateError("code");
    }
  },
  beforeDestroy() {
    clearInterval(this._timer);
  }
};
</script>
