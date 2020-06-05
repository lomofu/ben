<template>
  <v-card class="ma-5 d-flex align-center" min-height="400">
    <v-row justify="space-around">
      <div>
        <v-btn icon height="100" width="100" @click="handleLight">
          <v-icon size="80" color="yellow darken-1"
            >mdi-white-balance-sunny</v-icon
          >
        </v-btn>
        <br />
        <div class="ma-5">日光模式</div>
      </div>

      <div>
        <v-btn height="100" width="100" dark @click="handleDark">
          <v-icon size="80">mdi-weather-night</v-icon>
        </v-btn>
        <br />
        <div class="ma-5">暗黑模式</div>
      </div>
    </v-row>
  </v-card>
</template>

<script>
export default {
  name: "themeSelector",
  methods: {
    handleLight() {
      this.$store.commit("updateDark", false);
      this.$vuetify.theme.dark = false;
    },
    handleDark() {
      this.$store.commit("updateDark", true);
      this.$vuetify.theme.dark = true;
    }
  }
};
</script>

<style scoped></style>
