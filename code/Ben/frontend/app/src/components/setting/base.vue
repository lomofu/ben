<template>
  <div>
    <v-card class="ma-5" min-height="400">
      <div class="d-flex justify-center">
        <img src="../../../public/img/setting/setting-icon.png" />
      </div>
      <span class="d-flex justify-center pa-3">欢迎来到 Ben设置中心</span>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "base"
};
</script>
<style scoped>
div {
  margin: 0;
}
img {
  width: 300px;
  height: auto;
  margin: 30px;
}
span {
  font-family: PingFangBold, Microsoft YaHei, 黑体, sans-serif;
  font-weight: bolder;
  font-size: 35px;
  color: #4d4d4d;
}
</style>
