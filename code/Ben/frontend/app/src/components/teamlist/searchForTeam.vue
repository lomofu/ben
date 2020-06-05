<template>
  <div>
    <v-text-field
      v-model.trim="search"
      class="ma-3"
      label="搜索团队"
      style="max-width: 200px;"
      append-icon="mdi-magnify"
      clear-icon="mdi-close"
      outlined
      dense
      clearable
      hide-details
      v-on:click:clear="clear"
      v-on:click:append="searchTeam"
      @input="handleSearch"
    ></v-text-field>
  </div>
</template>

<script>
import debounce from "lodash/debounce";
import { REQUEST } from "../../common/view/Constant";

export default {
  name: "searchForTeam",
  props: {
    filter: {
      type: Object
    }
  },
  data: () => ({
    search: ""
  }),
  methods: {
    handleSearch: debounce(async function(val) {
      this.$emit("searchValue", val);
      if (val) {
        this.$store.commit("updateTeamSearchAll", false);
        const {
          data: { code, data }
        } = await this.$api.search.company.searchForTeam(this.filter, val);
        if (code === REQUEST.SUCCESS.code) {
          this.$emit("updateSearch", data);
        }
      }
    }, 300),
    async searchTeam() {
      let val = this.search;
      if (val) {
        this.$store.commit("updateTeamSearchAll", false);
        const {
          data: { code, data }
        } = await this.$api.search.company.searchForTeam(this.filter, val);
        if (code === REQUEST.SUCCESS.code) {
          this.$emit("updateSearch", data);
        }
      } else {
        this.$notification.warn("请输入要搜索的团队");
      }
    },
    clear() {
      this.search = null;
      this.$emit("searchValue", null);
      this.$emit("cancel");
    }
  },
  watch: {
    $route() {
      this.clear();
    }
  },
  created() {
    this.clear();
  },
  beforeDestroy() {
    this.clear();
  }
};
</script>

<style scoped></style>
