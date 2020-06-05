<template>
  <v-bottom-navigation
    :value="activeBtn"
    color="deep-purple accent-4"
    absolute
    app
  >
    <v-btn>
      <span>Recents</span>
      <v-icon>mdi-history</v-icon>
    </v-btn>

    <v-btn>
      <span>Favorites</span>
      <v-icon>mdi-heart</v-icon>
    </v-btn>

    <v-btn>
      <span>Nearby</span>
      <v-icon>mdi-map-marker</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "bottomNav",
  data() {
    return {
      activeBtn: 1
    };
  }
};
</script>

<style scoped></style>
