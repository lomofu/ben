<template>
  <div>
    <div class="d-flex justify-center text-center">
      <img
        class="load-img mr-3"
        src="../../../public/img/icon/icon-color.png"
        alt="icon"
      />
      <span class="load-word">Ben.</span>
      <div class="circular ml-5">
        <v-progress-circular indeterminate color="amber"></v-progress-circular>
      </div>
    </div>
    <p class="ml-3 font-italic text-center">
      <code class="mt-5 mr-3">{{ getEnv }}: {{ version }}</code>
    </p>
  </div>
</template>

<script>
export default {
  name: "load",
  data: () => ({
    version: null
  }),
  computed: {
    getEnv() {
      return process.env.VUE_APP_NAME;
    }
  },
  created() {
    this.version = this.$store.getters.getVersion;
  }
};
</script>
<style scoped>
.load-word {
  font-family: PingFangBold, Microsoft YaHei, 黑体, sans-serif;
  font-weight: bolder;
  font-size: 35px;
}
.load-img {
  height: 50px;
  width: 50px;
}
.circular::before {
  content: "";
  display: block;
  height: 10px;
  width: 10px;
}
</style>
