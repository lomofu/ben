<template>
  <div class="signup">
    <v-form v-show="getSignUp" ref="form">
      <v-text-field
        v-model="email"
        :error-messages="emailErrors"
        label="E-mail"
        :disabled="isDisable"
        required
        clear-icon="mdi-close"
        clearable
        @input="email && $v.email.$touch()"
        @blur="email && $v.email.$touch()"
        v-on:click:clear="reset"
        :height="height"
        :placeholder="btn.placeholder"
        :prepend-inner-icon="btn.icon"
        solo
      ></v-text-field>

      <v-btn
        @click="handleSubmit"
        block
        class="btn-word"
        :loading="isDisable"
        dark
        :height="btnHeight"
        >{{ btn.word }}
      </v-btn>
    </v-form>
    <div v-if="!getSignUp" class="animated fadeIn">
      <p class="has-clicked">{{ hasClicked.msg }}</p>
      <p class="set-email">
        <v-icon size="80" color="white"> {{ hasClicked.icon2 }} </v-icon>
      </p>
      <router-link to="/">
        <v-btn
          block
          class="btn-word"
          dark
          height="50"
          @click="$store.dispatch('asyncUpdateShowUnSignUpView', true)"
          >{{ btn.word2 }}
        </v-btn>
      </router-link>
    </div>
  </div>
</template>

<script>
import { validationMixin } from "vuelidate";
import { required, email } from "vuelidate/lib/validators";
import { HOME, REQUEST } from "../../common/view/Constant";

export default {
  name: "signup",
  mixins: [validationMixin],

  validations: {
    email: { required, email }
  },
  props: ["height", "btnHeight"],
  data: () => ({
    isDisable: false,
    btn: {
      icon: "mdi-at",
      word: "免 费 注 册",
      word2: "返 回 主 页",
      placeholder: "请输入你的邮箱"
    },
    hasClicked: {
      icon2: "mdi-email-check",
      msg: "邮件已发送至您的邮箱, 请前往你的邮箱验证!"
    },
    email: ""
  }),
  methods: {
    handleSubmit() {
      const validate = this.$v;
      validate.$touch();
      if (validate.email.required && validate.email.email) {
        let email = this.email;
        this.sendEmail(email);
      } else {
        return false;
      }
    },
    async sendEmail(email) {
      const me = this;
      me.isDisable = true;
      try {
        let {
          data: { code }
        } = await me.$api.signup.signup(email);
        if (code === REQUEST.SUCCESS.code) {
          this.$store.dispatch("asyncUpdateShowUnSignUpView", false);
        } else {
          me.isDisable = false;
        }
      } finally {
        me.isDisable = false;
      }
    },
    reset() {
      this.isDisable = false;
      this.email = "";
      this.$v.$reset();
    }
  },
  computed: {
    emailErrors() {
      const errors = [];
      const validate = this.$v;
      if (!validate.email.$dirty) return errors;
      !validate.email.email && errors.push(HOME.TIPS.EMAIL_ILLEGAL);
      !validate.email.required && errors.push(HOME.TIPS.EMAIL_IS_EMPTY);
      return errors;
    },
    getSignUp() {
      return this.$store.getters.getShowUnSignUpView;
    }
  }
};
</script>

<style lang="scss" scoped>
.signup {
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

  .has-clicked {
    @include font-light;
    font-size: 27px;
    color: white;
    text-shadow: $text-shadow;
    padding: 10px;
    &::after {
      @include before-after(30px);
    }
  }
}
</style>
