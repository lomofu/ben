<template>
  <v-form style="min-width: 350px;max-width: 500px;" class="login-panel">
    <v-text-field
      label="请输入邮箱或者手机"
      v-model.trim="user.account"
      required
      clearable
      clear-icon="mdi-close"
      :disabled="isDisable"
      @input="$v.user.account.$touch()"
      @blur="user.account && $v.user.account.$touch()"
      :error-messages="accountErrors"
    ></v-text-field>
    <v-text-field
      label="请输入密码"
      :type="showPass ? 'text' : 'password'"
      :append-icon="showPass ? 'mdi-eye' : 'mdi-eye-off'"
      v-model.trim="user.password"
      clearable
      clear-icon="mdi-close"
      :disabled="isDisable"
      @input="$v.user.password.$touch()"
      @blur="user.password && $v.user.password.$touch()"
      :error-messages="passwordErrors"
      @click:append="showPass = !showPass"
      required
    ></v-text-field>
    <div class="d-flex justify-space-between" style="width: 100%">
      <v-switch
        v-model="user.isRememberMe"
        :label="user.isRememberMe ? '记住我' : '不要记住我'"
      ></v-switch>
      <v-btn
        text
        class="mt-3"
        color="primary"
        @click="$store.commit('updateIsPassword', false)"
      >
        <span>短信验证码登陆</span>
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
      >
        登 录
      </v-btn>
    </span>
  </v-form>
</template>
<script>
import md5 from "js-md5";
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";
import { REQUEST } from "../../common/view/Constant";
import { ACCOUNT } from "../../common/validator/Constant";

export default {
  name: "loginPanel",
  mixins: [validationMixin],
  validations: {
    user: {
      account: {
        required
      },
      password: {
        required
      }
    }
  },
  data: () => ({
    isDisable: false,
    showPass: false,
    user: {
      account: null,
      password: null,
      isRememberMe: false
    }
  }),

  methods: {
    validateFormData() {
      const validate = this.$v.user;
      validate.$touch();
      return validate.account.$invalid || validate.password.$invalid;
    },
    async handleLogin() {
      const me = this;
      if (this.validateFormData()) {
        return false;
      }
      try {
        me.isDisable = true;
        const user = me.user;
        const {
          data: { code, msg, data }
        } = await this.$api.login.login(
          user.account,
          md5(user.password),
          user.isRememberMe
        );
        if (code === REQUEST.SUCCESS.code) {
          this.$msg.success(msg);
          this.$cookies.set("ben-user", data);
          setTimeout(() => {
            window.location.replace(process.env.VUE_APP_CENTER);
          }, 700);
        }
      } catch (e) {
        me.isDisable = false;
      }
    },
    reset() {
      this.isDisable = false;
      const user = this.user;
      user.account = null;
      user.password = null;
      this.$v.$reset();
    }
  },
  computed: {
    accountErrors() {
      const errors = [];
      if (!this.$v.user.account.$dirty) return errors;
      !this.$v.user.account.required && errors.push(ACCOUNT.ACCOUNT_IS_EMPTY);
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.user.password.$dirty) return errors;
      !this.$v.user.password.required && errors.push(ACCOUNT.PASSWORD_IS_EMPTY);
      return errors;
    }
  }
};
</script>
