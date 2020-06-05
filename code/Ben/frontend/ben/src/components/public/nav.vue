<template>
  <div>
    <v-row>
      <v-app-bar :dark="light" :elevate-on-scroll="true" fixed class="nav-bar">
        <v-col cols="12" xs="2" sm="2" md="2" lg="2" xl="2" class="d-flex">
          <router-link to="/">
            <div class="d-flex">
              <v-icon :left="true" size="45" class="nav-icon">mdi-grain</v-icon>
              <span class="nav-title">Ben.</span>
              <span class="nav-title ml-3 red--text">({{ getEnv }})</span>
            </div>
          </router-link>
        </v-col>

        <v-col class="d-flex d-sm-none nav-phone-btn">
          <v-btn small outlined @click="showMenu = !showMenu">
            <v-icon dark>mdi-format-list-bulleted-square</v-icon>
          </v-btn>
          <v-expand-transition>
            <v-list
              light
              v-show="showMenu"
              class="phone-showMeun text-center"
              @mouseleave="handleLeave"
              elevation="5"
            >
              <v-list-item v-ripple class="list-item" v-show="showHomeBtn">
                <router-link to="/"
                  ><v-icon color="#1e73a8" size="25">{{
                    phoneIcon.home[1]
                  }}</v-icon
                  ><span class="phone-icon"> 首页</span></router-link
                >
              </v-list-item>
              <v-list-item v-ripple class="list-item" v-show="showPriceBtn">
                <router-link to="/price"
                  ><v-icon color="#1e73a8" size="25">{{
                    phoneIcon.price[1]
                  }}</v-icon
                  ><span class="phone-icon"> 价格</span></router-link
                >
              </v-list-item>
              <v-list-item v-ripple class="list-item" v-show="showSuppBtn">
                <router-link to="/support"
                  ><v-icon color="#1e73a8" size="25">{{
                    phoneIcon.support[1]
                  }}</v-icon
                  ><span class="phone-icon"> 支持</span></router-link
                >
              </v-list-item>
              <v-list-item v-ripple class="list-item">
                <router-link to="/login"
                  ><v-icon color="#1e73a8" size="25">{{
                    phoneIcon.login[1]
                  }}</v-icon
                  ><span class="phone-icon"> 登录</span></router-link
                >
              </v-list-item>
              <v-list-item v-ripple class="list-item">
                <router-link to="/signup"
                  ><v-icon color="#1e73a8" size="25">{{
                    phoneIcon.signup[1]
                  }}</v-icon
                  ><span class="phone-icon"> 注册</span></router-link
                >
              </v-list-item>
            </v-list>
          </v-expand-transition>
        </v-col>

        <v-col
          cols="12"
          xs="10"
          md="10"
          sm="10"
          lg="10"
          xl="10"
          style="text-align: end"
          class="d-none d-sm-table"
        >
          <router-link to="/" v-show="showHomeBtn">
            <v-btn class="nav-btn-select" text>首页</v-btn>
          </router-link>
          <router-link to="/price" v-show="showPriceBtn">
            <v-btn class="nav-btn-select" text>价格</v-btn>
          </router-link>
          <router-link to="/support" v-show="showSuppBtn"
            ><v-btn class="nav-btn-select" text>支持</v-btn></router-link
          >
          <router-link to="/login"
            ><v-btn class="btn-select" text>登录</v-btn></router-link
          >
          <router-link to="/signup">
            <v-btn class="nav-btn-select" outlined>
              <v-icon style="margin-right: 5px"
                >mdi-account-multiple-plus-outline</v-icon
              >注册
            </v-btn>
          </router-link>
        </v-col>
      </v-app-bar>
    </v-row>
  </div>
</template>

<script>
export default {
  name: "Nav",
  data: () => ({
    light: true,
    showMenu: false,
    showHomeBtn: true,
    showPriceBtn: true,
    showSuppBtn: true,
    phoneIcon: {
      color: "#1e73a8",
      home: ["25", "mdi-home"],
      price: ["25", "mdi-currency-usd"],
      support: ["25", "mdi-lightbulb-on"],
      login: ["25", "mdi-account-circle"],
      signup: ["25", "mdi-account-plus"]
    }
  }),
  methods: {
    handleLeave() {
      let me = this;
      setTimeout(() => {
        me.showMenu = !me.showMenu;
      }, 2000);
    }
  },
  computed: {
    getEnv() {
      return process.env.VUE_APP_NAME;
    }
  },
  created() {
    let path = this.$route.path;
    switch (path) {
      case "/":
      case "/home": {
        this.showHomeBtn = !this.showHomeBtn;
        break;
      }
      case "/price": {
        this.showPriceBtn = !this.showPriceBtn;
        break;
      }
      case "/support": {
        this.showSuppBtn = !this.showSuppBtn;
        break;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/public/nav.scss";
</style>
