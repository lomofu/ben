<template>
  <v-footer :style="{ background: dark ? '#0a0a0a' : 'white' }" :dark="dark">
    <v-row class="d-flex justify-center ma-2">
      <strong style="font-size: 15px!important;">
        © {{ new Date().getFullYear() }} , Ben. Web Services
      </strong>
    </v-row>
  </v-footer>
</template>

<script>
export default {
  name: "benFooter",
  computed: {
    dark() {
      return this.$store.getters.getDark;
    }
  }
};
</script>

<style scoped></style>
