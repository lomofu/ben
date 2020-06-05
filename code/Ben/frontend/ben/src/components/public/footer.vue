<template>
  <v-footer padless width="100%" class="white mt-12 text-center">
    <p class="d-flex justify-space-between  container">
      <v-card-text class="mr-12">
        <img src="../../../public/img/icon/icon-color.png" alt="" />
        <span class="footer-ben ml-3 mb-3">Ben.</span><br />
        <v-btn icon><v-icon> </v-icon></v-btn>
      </v-card-text>
      <v-card-text class="fooeter-support mt-3 mb-2">
        <strong>服 务</strong><br /><br />
        <router-link to="/">首页</router-link><br />
        <router-link to="/price">价格</router-link><br />
        <router-link to="/login">登陆</router-link><br />
        <router-link to="/signup">注册</router-link><br />
      </v-card-text>
      <v-card-text class="fooeter-support mt-3 mb-2">
        <strong>帮 助</strong><br /><br />
        <router-link to="/support">支持<br /></router-link>
      </v-card-text>
      <v-card-text class="fooeter-support mt-3 mb-2">
        <strong>联系我们</strong><br /><br />
        <span class=""> ben_k8s@163.com</span><br /><br />
        <v-icon>mdi-account-box</v-icon>
        <span> Lomo Fu</span><br /><br />
        <a href="https://github.com/lomofu"
          ><v-icon color="green">mdi-wechat</v-icon></a
        >
        <a href="https://github.com/lomofu"><v-icon>mdi-github-box</v-icon></a
        >&nbsp;
        <a href="http://180.76.240.3/users/sign_in"
          ><v-icon color="orange">mdi-gitlab</v-icon></a
        >&nbsp;
      </v-card-text>
    </p>

    <v-card width="100%" class="text-center mt-12">
      <v-card-text class="footer mt-2 pa-1">
        <strong style="font-size: 15px!important;">
          © {{ new Date().getFullYear() }} , Ben. Web Services</strong
        >
      </v-card-text>
    </v-card>
  </v-footer>
</template>
<style lang="scss" scoped>
@import "@/assets/css/public/footer.scss";
</style>
