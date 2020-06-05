<template>
  <div>
    <v-row v-if="thisWeek || total">
      <v-col cols="12" xs="12" sm="12" md="6" lg="6" xl="6">
        <v-card-subtitle>
          本周任务:
        </v-card-subtitle>
        <div class="d-flex justify-center" style="font-size: 45px">
          {{ thisWeek ? thisWeek : 0 }}
        </div>
      </v-col>
      <v-divider vertical class="ma-3"></v-divider>
      <v-col cols="12" xs="12" sm="12" md="5" lg="5" xl="5">
        <v-card-subtitle>
          所有任务:
        </v-card-subtitle>
        <div class="d-flex justify-center" style="font-size: 45px">
          {{ total ? total : 0 }}
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "countCard",
  computed: {
    ...mapGetters({
      thisWeek: "getMyJobThisWeekCount",
      total: "getMyJobTotalCount"
    })
  }
};
</script>

<style scoped></style>
