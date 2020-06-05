<template>
  <div>
    <index-nav></index-nav>
    <v-row>
      <v-col>
        <div class="home-type">
          <v-row justify="center" class="home-type-content">
            <vue-typed-js
              :autoInsertCss="true"
              :backSpeed="20"
              :startDelay="300"
              :strings="config.typecontent"
              :typeSpeed="40"
              @onComplete="handleComplete"
              class="typing-title"
            >
              <h1>
                <v-icon color="white" size="55px" style="margin-right: 14px"
                  >{{ config.icon }}
                </v-icon>
                <span class="typing"></span>
              </h1>
            </vue-typed-js>
          </v-row>
          <v-form v-if="showUnsignUpView">
            <transition :duration="3000" name="slide-fade">
              <v-row justify="center">
                <v-col cols="12" lg="4" md="4" sm="4" xl="4" xs="4">
                  <transition name="slide-fade">
                    <v-text-field
                      :error-messages="emailErrors"
                      @blur="$v.email.$touch()"
                      @input="$v.email.$touch()"
                      clear-icon="mdi-close"
                      clearable
                      :disabled="isDisable"
                      :height="config.btn.height"
                      :placeholder="config.btn.placeholder"
                      :prepend-inner-icon="config.btn.icon"
                      required
                      solo
                      transition="scroll-y-reverse-transition"
                      v-model="email"
                      v-show="config.showInput"
                    ></v-text-field>
                  </transition>
                </v-col>
              </v-row>
            </transition>
            <v-row justify="center">
              <v-col cols="12" lg="4" md="4" sm="4" xl="4" xs="4">
                <v-btn
                  @click="handleSubmit"
                  block
                  class="home-btn-word"
                  dark
                  height="50"
                  :loading="isDisable"
                  v-show="config.showInput"
                  >{{ config.btn.word }}
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
          <v-row justify="center">
            <v-col cols="12" lg="4" md="4" sm="4" xl="4" xs="4">
              <div v-if="!showUnsignUpView" class="animated fadeIn">
                <p class="has-clicked">{{ hasClicked.msg }}</p>
                <p class="set-email">
                  <v-icon size="80" color="white">
                    {{ hasClicked.icon2 }}
                  </v-icon>
                </p>
                <v-btn
                  block
                  class="btn-word"
                  dark
                  height="50"
                  @click="acknowlegal"
                  >{{ btn.word2 }}
                </v-btn>
              </div>
            </v-col>
          </v-row>
        </div>
      </v-col>
    </v-row>

    <v-row align="center" justify="center" class="home-row ma-10">
      <v-col cols="12">
        <div>
          <v-row>
            <v-col
              cols="12"
              class="wow fadeInUp"
              data-wow-duration="0.5s"
              data-wow-offset="250"
              lg="6"
              md="6"
              sm="12"
              xl="6"
              xs="12"
              align-self="center"
            >
              <p class="home-row-title text-center">
                {{ config.content.p1[0] }}
              </p>
              <div class="d-flex justify-center">
                <p class="home-row-content">
                  {{ config.content.p1[1] }}
                </p>
              </div>
            </v-col>
            <v-col
              class="home-row-img1"
              cols="12"
              lg="6"
              md="6"
              sm="12"
              xl="5"
              xs="12"
            >
              <schedule-animation></schedule-animation>
            </v-col>
          </v-row>
        </div>
      </v-col>
    </v-row>

    <v-lazy
      transition="fade-transition"
      :options="{
        rootMargin: '0px',
        threshold: 1
      }"
    >
      <v-row align="center" justify="center" class="home-row p2">
        <v-col cols="12" class="p2-col">
          <div>
            <v-row>
              <v-spacer></v-spacer>
              <v-col
                class="text-left ma-5"
                cols="12"
                xl="5"
                lg="5"
                md="5"
                sm="12"
                xs="12"
              >
                <speed></speed>
              </v-col>
              <v-col cols="12" lg="6" md="6" sm="12" xs="12" class="mt-12">
                <p class="text-center home-row-title">
                  {{ config.content.p2[0] }}
                </p>
                <div class="d-flex justify-center">
                  <p class="home-row-content">
                    {{ config.content.p2[1] }}
                  </p>
                </div>
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-lazy>

    <v-lazy transition="fade-transition">
      <v-row align="center" justify="center" class="home-row ma-10">
        <v-col cols="12">
          <div>
            <v-row>
              <v-col
                cols="12"
                lg="6"
                md="6"
                sm="12"
                xl="6"
                xs="12"
                class="mt-12"
              >
                <p class="text-center home-row-title">
                  {{ config.content.p3[0] }}
                </p>
                <div class="d-flex justify-center">
                  <p class="home-row-content">{{ config.content.p3[1] }}</p>
                </div>
              </v-col>
              <v-spacer></v-spacer>
              <v-col
                class="d-flex justify-center ma-3"
                cols="12"
                lg="5"
                md="5"
                sm="12"
                xl="5"
                xs="12"
              >
                <broadcast />
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-lazy>
    <v-divider class="ma-10"></v-divider>
    <v-lazy transition="fade-transition">
      <v-row align="center" justify="center" class="home-row">
        <v-col cols="12">
          <div>
            <v-row>
              <v-col
                cols="12"
                lg="6"
                md="6"
                sm="12"
                xl="6"
                xs="12"
                class="d-flex justify-center align-center"
              >
                <v-img
                  style="margin-left: auto;margin-right: auto"
                  class="home-row-img3"
                  src="../../public/img/home/devices.png"
                  alt="device"
                  eager
                  :aspect-ratio="2"
                  max-width="500px"
                  height="100%"
                  contain
                />
              </v-col>
              <v-col cols="12" lg="6" md="6" sm="12" xl="6" xs="12">
                <p class="text-center home-row-title">
                  {{ config.content.p4[0] }}
                </p>
                <p class="home-row-content">{{ config.content.p4[1] }}</p>
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-lazy>
    <v-lazy transition="fade-transition">
      <v-sheet
        color="#65459A"
        height="150"
        class="d-flex justify-space-around align-center mb-12"
      >
        <v-row>
          <v-col class="ma-2 col-sm-12 col-md-6 d-flex justify-center">
            <span class="view-price">{{ config.content.view[0] }}</span>
          </v-col>
          <v-col class="ma-2  d-flex justify-center">
            <router-link to="/price">
              <v-btn outlined color="white" min-width="120" min-height="50">{{
                config.content.view[1]
              }}</v-btn>
            </router-link>
          </v-col>
        </v-row>
      </v-sheet>
    </v-lazy>
    <v-lazy transition="fade-transition">
      <v-row align="center" justify="center">
        <v-col class="intro col-12 text-center">
          <p class="intro-title">Ben可以适用于许多行业</p>
          <div class="intro-container d-flex justify-center mb-5 mt-2">
            <div class="intro-content">餐饮</div>
            <div class="intro-content">IT</div>
            <div class="intro-content">Teams</div>
            <div class="intro-content">...</div>
          </div>
          <v-img
            src="../../public/img/home/business.png"
            eager
            style="margin-left: auto;margin-right: auto"
            :aspect-ratio="1"
            max-width="400px"
            height="100%"
            contain
          />
        </v-col>
      </v-row>
    </v-lazy>
    <index-footer></index-footer>
  </div>
</template>

<script>
import { validationMixin } from "vuelidate";
import { email } from "vuelidate/lib/validators";
import { WOW } from "wowjs";
import { HOME, REQUEST } from "../common/view/Constant";

export default {
  name: "home",
  inject: ["reload"],
  components: {
    indexNav: () => import("../components/public/nav.vue"),
    speed: () => import("../components/home/speed.vue"),
    broadcast: () => import("../components/home/broadcast.vue"),
    indexFooter: () => import("../components/public/footer.vue"),
    scheduleAnimation: () => import("../components/home/schedule.vue")
  },
  mixins: [validationMixin],
  validations: {
    email: { email }
  },
  data: () => ({
    config: {
      typecontent: HOME.VIEW.TYPECONTENT,
      btn: HOME.VIEW.BTN,
      content: HOME.VIEW.CONTENT,
      show: true,
      showInput: false,
      icon: "mdi-format-list-bulleted"
    },
    hasClicked: {
      icon2: "mdi-email-check",
      msg: "邮件已发送至您的邮箱, 请前往你的邮箱验证!"
    },
    btn: {
      word2: "确 认"
    },
    isDisable: false,
    showUnsignUpView: true,
    email: null
  }),
  methods: {
    handleComplete() {
      this.config.showInput = true;
    },
    handleSubmit() {
      if (this.email === null) {
        this.$router.push({ path: "/signup" });
      } else {
        this.$v.email.$touch();
        if (this.$v.email.email) {
          this.sendEmail(this.email);
        }
      }
    },
    acknowlegal() {
      this.reset();
    },
    async sendEmail(email) {
      const me = this;
      try {
        me.isDisable = true;
        const {
          data: { code, msg }
        } = await me.$api.signup.signup(email);
        if (code === REQUEST.SUCCESS.code) {
          this.showUnsignUpView = false;
        } else {
          me.$router.push({ path: "signup", query: { msg: msg } });
        }
      } catch (e) {
        me.$router.push({ path: "signup", query: { msg: e.message } });
      } finally {
        me.isDisable = false;
      }
    },
    reset() {
      this.reload();
      this.showUnsignUpView = true;
      this.isDisable = false;
      this.email = "";
      this.$v.$reset();
    }
  },
  computed: {
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.email && errors.push(HOME.TIPS.EMAIL_ILLEGAL);
      return errors;
    }
  },
  mounted() {
    var wow = new WOW({
      boxClass: "wow",
      animateClass: "animated",
      offset: 0,
      mobile: true,
      live: true
    });
    wow.init();
  },
  beforeRouteLeave(to, from, next) {
    this.$store.dispatch("asyncUpdateShowUnSignUpView", true);
    this.reset();
    next();
  }
};
</script>
<style lang="scss" scoped>
@import "@/assets/css/home/home.scss";
</style>
