<template>
  <v-row justify="center" v-show="load">
    <div class="load"></div>
    <v-progress-circular
      class="circle"
      :size="50"
      :width="7"
      color="purple"
      indeterminate
    ></v-progress-circular>
    <div class="text mt-5">加载中...</div>
  </v-row>
</template>

<script>
export default {
  name: "contentLoading",
  computed: {
    load: {
      get() {
        return this.$store.getters.getLoading;
      },
      set() {}
    }
  }
};
</script>

<style scoped>
.load {
  width: 100%;
  height: 100%;
  background: white;
  position: absolute;
  z-index: 5;
  opacity: 60%;
}
.circle {
  position: absolute;
  top: 20%;
  z-index: 3;
}
.text {
  position: absolute;
  z-index: 3;
  top: 25%;
  font-weight: normal;
}
</style>
