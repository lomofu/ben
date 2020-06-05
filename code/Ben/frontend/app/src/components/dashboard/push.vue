<template>
  <v-snackbar v-model="pushing" color="orange" :timeout="2000" bottom>
    <span class="ml-5"> 推送中</span>
    <span class="custom-loader mr-5">
      <v-icon color="white">mdi-cached</v-icon>
    </span>
  </v-snackbar>
</template>

<script>
import { eventBus } from "../../bus";
export default {
  name: "push",
  data: () => ({
    pushing: false
  }),
  created() {
    eventBus.$on("pushing", index => {
      this.pushing = index;
    });
  },
  beforeDestroy() {
    eventBus.$off("pushing");
  }
};
</script>

<style scoped>
.custom-loader {
  animation: loader 1s infinite;
  display: flex;
}
@-moz-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-webkit-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-o-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
