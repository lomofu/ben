<template>
  <v-row class="login" justify="center">
    <v-col
      cols="12"
      xs="12"
      sm="12"
      md="4"
      lg="4"
      xl="3"
      class="login-left d-sm-none d-md-table-row animated fadeInLeft"
      align="center"
    >
      <v-img
        src="../../public/img/login/login.png"
        height="100%"
        :aspect-ratio="1"
        eager
      />
    </v-col>
    <v-col
      cols="12"
      xs="12"
      sm="12"
      md="8"
      lg="8"
      xl="9"
      class="animated fadeInRight"
    >
      <div class="login-btn-back pa-1">
        <v-btn
          class="ma-2"
          outlined
          color="deep-purple lighten-1"
          @click="$router.push({ path: '/' })"
        >
          <v-icon> {{ content.btn.icon }}</v-icon>
          {{ content.btn.word }}</v-btn
        >
      </div>
      <v-row>
        <v-col
          cols="12"
          xs="12"
          sm="12"
          md="12"
          lg="12"
          xl="12"
          class="text-center"
        >
          <v-row justify="center">
            <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12">
              <router-link to="/">
                <img
                  src="../../public/img/icon/icon-color.png"
                  alt="icon"
                  class="mb-3"
                />
              </router-link>
              <br />
              <router-link to="/" class="mr-3">
                <span class="login-ben">Ben.</span>
              </router-link>
            </v-col>
          </v-row>
          <p class="text-center login-content-subtitle ">
            {{ content.subtitle }}
          </p>
          <v-row class="mt-3">
            <v-col
              cols="12"
              xs="12"
              sm="12"
              md="12"
              lg="12"
              xl="12"
              align="center"
            >
              <password-panel
                v-if="getIsPassword"
                class="panel animated fadeIn"
                ref="password"
              />
              <code-panel
                v-if="!getIsPassword"
                class=" panel animated fadeIn"
              ></code-panel>
            </v-col>
          </v-row>
          <div>
            <v-row justify="center">
              <v-btn class="word" v-ripple text>
                <router-link to="/">忘记密码?</router-link>
              </v-btn>
            </v-row>
            <v-row justify="center">
              <span class="mt-2">没有账号?</span>
              <v-btn class="word" v-ripple text>
                <router-link to="/signup">创建一个新账号</router-link>
              </v-btn>
            </v-row>
            <v-row justify="center" class="mt-2">
              <span v-ripple class="pa-1">
                <router-link to="/price">产品价格</router-link>
              </span>
              <span class="ml-2 mr-2 pa-1">|</span>
              <span v-ripple class="pa-1">
                <router-link to="support">技术支持</router-link>
              </span>
            </v-row>
            <p class="s-word mt-3" v-ripple></p>
          </div>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import { LOGIN } from "../common/view/Constant";
export default {
  name: "login",
  components: {
    passwordPanel: () => import("../components/login/loginPanel.vue"),
    codePanel: () => import("../components/login/loginWithCode.vue")
  },
  data: () => ({
    content: LOGIN.VIEW
  }),
  computed: {
    getIsPassword() {
      return this.$store.getters.getIsPassword;
    }
  },
  beforeRouteLeave(to, from, next) {
    if (this.$refs.password) this.$refs.password.reset();
    next();
  }
};
</script>
<style lang="scss">
@import "@/assets/css/login/login.scss";
</style>
