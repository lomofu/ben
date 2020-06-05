<template>
  <div class="register">
    <v-btn
      class="btn ma-2"
      outlined
      color="#424242"
      @click="$router.push({ path: '/' })"
    >
      <v-icon> {{ content.btn.icon }}</v-icon>
      {{ content.btn.word }}</v-btn
    >
    <v-row class="text-center register-title animated fadeInDown">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12">
        <v-icon class="register-title-icon" color="white">mdi-grain</v-icon>
        <span class="register-title-word">Ben.</span>
      </v-col>
    </v-row>

    <v-row class="text-center animated fadeInDown" v-show="getSignUp">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12">
        <p class="register-subtitle">{{ content.subtitle }}</p>
      </v-col>
    </v-row>

    <v-row class="text-center animated fadeInUp">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <signup height="70" btnHeight="50" class="signup" ref="signup"></signup>
      </v-col>
    </v-row>

    <v-row class="text-center fadeInUp" v-show="getSignUp">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <p class="content1 text-center">
          {{ content.word1 }} <router-link to="/login"> 点击登录</router-link>
        </p>
      </v-col>
    </v-row>
    <p class="text-center fadeInUp subinfo">
      <router-link to="/price">产品价格</router-link> |
      <router-link to="/support">技术支持</router-link>
    </p>
  </div>
</template>

<script>
import { SIGNUP } from "../common/view/Constant";
export default {
  name: "SignUp",
  components: {
    signup: () => import("../components/signup/signup.vue")
  },
  data: () => ({
    content: SIGNUP.VIEW
  }),
  computed: {
    getSignUp() {
      return this.$store.getters.getShowUnSignUpView;
    }
  },
  beforeRouteLeave(to, from, next) {
    this.$store.dispatch("asyncUpdateShowUnSignUpView", true);
    this.$refs.signup.reset();
    next();
  }
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/signup/signup.scss";
</style>
