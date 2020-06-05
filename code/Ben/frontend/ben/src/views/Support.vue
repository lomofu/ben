<template>
  <div class="support">
    <indexNav></indexNav>
    <div class="support-main animated fadeInDown">
      <p class="text-center support-main-title">{{ config.main.title }}</p>
      <div class="support-main-icon" />
    </div>
    <v-row
      align="center"
      class="support-content animated fadeInUp d-flex justify-center"
      id="next"
    >
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <p class="supp-title">
          {{ config.main.contents[0].title }}
        </p>
      </v-col>
      <v-chip class="ma-2" color="indigo" label text-color="white" large chip>
        version: {{ $store.getters.getVersion }}
      </v-chip>
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <p>
          <v-icon size="80" color="blue" class="content1-icon">{{
            config.main.contents[1].icon
          }}</v-icon>
        </p>
        <p>{{ config.main.contents[1].title }}</p>
        <br />
        <v-btn outlined @click="$msg.warn('还在开发中,不要着急')">{{
          config.main.contents[1].content
        }}</v-btn>
      </v-col>
    </v-row>
    <v-divider class="ma-10"></v-divider>
    <v-row align="center" class="support-content1">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <img class="img1" src="../../public/img/support/lock.png" alt="lock" />
        <p class="text-center title1">
          {{ config.main.content1[0] }}
        </p>
        <p class="text-center">
          {{ config.main.content1[1] }}
          <!--todo-->
          <a @click="$msg.warn('还在开发中,不要着急')">找回密码</a>
        </p>
      </v-col>
    </v-row>
    <v-divider class="ma-10"></v-divider>
    <v-row align="center" justify="center" class="support-content2">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <v-icon size="60" color="orange" class="content2-icon"
          >mdi-firefox</v-icon
        >
        <img class="img2" src="../../public/img/home/chrome.png" alt="chrome" />
        <v-icon size="50" color="blue" class="content2-icon"
          >mdi-edge-legacy</v-icon
        >
      </v-col>
      <v-col
        cols="12"
        xs="12"
        sm="12"
        md="12"
        lg="12"
        xl="12"
        align="center"
        class="mt-2"
      >
        <p class="title2">
          {{ config.main.content2[0] }}
        </p>
        <p>
          {{ config.main.content2[1] }}
        </p>
        <br />
      </v-col>

      <v-col
        class="offset-xs-5"
        cols="12"
        xs="12"
        sm="6"
        md="6"
        lg="6"
        xl="6"
        align="center"
      >
        <br />
        <v-card outlined>
          <br />
          <v-row>
            <v-col
              cols="12"
              xs="6"
              sm="6"
              md="6"
              lg="6"
              xl="6"
              v-for="(item, index) in config.main.supp_list"
              :key="index"
            >
              <template>
                <p>
                  <v-icon size="50" :color="item.color">{{ item.icon }}</v-icon>
                </p>
                <p class="ma-2">{{ item.supp }}</p>
                <p>
                  <v-icon :color="item.color2">{{ item.icon2 }}</v-icon>
                </p>
                <br />
                <br />
              </template>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <br />
    <v-divider class="ma-10"></v-divider>
    <v-row align="center" class="support-content3">
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <v-icon size="50" color="green" class="content3-icon"
          >mdi-chat-processing</v-icon
        >
        <img
          class="img3"
          src="../../public/img/icon/icon-color.png"
          alt="icon"
        />
        <v-icon size="60" color="blue" class="content3-icon">mdi-email</v-icon>
      </v-col>
      <v-col cols="12" xs="12" sm="12" md="12" lg="12" xl="12" align="center">
        <p class="contact">
          {{ config.main.content3[0] }}
        </p>
        <p v-html="config.main.content3[1]"></p>
      </v-col>
    </v-row>
    <index-footer></index-footer>
  </div>
</template>

<script>
import { SUPPORT } from "../common/view/Constant";

export default {
  name: "Support",
  components: {
    indexNav: () => import("../components/public/nav.vue"),
    indexFooter: () => import("../components/public/footer.vue")
  },
  data: () => ({
    config: {
      main: SUPPORT.VIEW
    }
  })
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/support/support.scss";
</style>
