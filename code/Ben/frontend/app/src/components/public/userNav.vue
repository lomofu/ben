<template>
  <v-app-bar :color="color" dark app :elevate-on-scroll="true">
    <v-app-bar-nav-icon @click="resizeNav" class="hidden-sm-and-down">
      <v-icon>{{
        getMiniIndex ? "mdi-chevron-right" : "mdi-chevron-left"
      }}</v-icon>
    </v-app-bar-nav-icon>
    <v-toolbar-title>{{ title }}</v-toolbar-title>
    <v-spacer></v-spacer>
    <div class="time">{{ date }}</div>
    <v-spacer></v-spacer>
    <slot></slot>
  </v-app-bar>
</template>

<script>
export default {
  name: "userNav",
  props: ["title", "color"],
  data: () => ({
    date: new Date()
  }),
  methods: {
    resizeNav() {
      const store = this.$store;
      store.commit("updateMiniIndex", !this.getMiniIndex);
    }
  },
  computed: {
    getMiniIndex() {
      const store = this.$store;
      return store.getters.getMiniIndex;
    }
  },
  created() {
    let _this = this;
    this.timer = setInterval(function() {
      _this.date = _this.$moment().format("llll");
    });
  },
  beforeDestroy: function() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
};
</script>

<style scoped></style>
