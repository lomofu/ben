<template>
  <v-container fluid>
    <div
      class="text-center font-italic"
      style="font-family: PingFangLight,sans-serif;font-size: 30px;"
    >
      效率%
    </div>
    <v-sparkline
      :value="value"
      :gradient="gradient"
      :smooth="radius || false"
      :padding="padding"
      :line-width="width"
      :stroke-linecap="lineCap"
      :gradient-direction="gradientDirection"
      :fill="fill"
      :type="type"
      :auto-line-width="autoLineWidth"
      :auto-draw="autodraw"
      show-labels
    ></v-sparkline>
  </v-container>
</template>
<script>
const gradients = [
  ["#222"],
  ["#a3a3a3"],
  ["red", "orange", "yellow"],
  ["purple", "violet"],
  ["#74f1db", "#F0F", "#FF0"],
  ["#f71d42", "#ffd200", "#9ca382"]
];

export default {
  data: () => ({
    width: 4,
    radius: 30,
    padding: 8,
    lineCap: "round",
    gradient: gradients[5],
    value: [0, 2, 30, 60, 100],
    gradientDirection: "top",
    gradients,
    fill: false,
    type: "trend",
    autoLineWidth: true,
    autodraw: true
  })
};
</script>
<style scoped>
path {
  transition-delay: 500ms;
}
</style>
