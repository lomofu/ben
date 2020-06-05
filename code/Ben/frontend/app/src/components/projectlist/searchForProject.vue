<template>
  <div>
    <v-text-field
      v-model.trim="search"
      class="ma-3"
      label="搜索项目"
      style="max-width: 200px;"
      append-icon="mdi-magnify"
      clear-icon="mdi-close"
      outlined
      dense
      clearable
      hide-details
      v-on:click:clear="clear"
      v-on:click:append="searchProject"
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
    search: null
  }),
  methods: {
    handleSearch: debounce(async function(val) {
      this.$emit("searchValue", val);
      if (val) {
        this.$store.commit("updateProjectSearchAll", false);
        const {
          data: { code, data }
        } = await this.$api.search.company.searchForProject(this.filter, val);
        if (code === REQUEST.SUCCESS.code) {
          this.$emit("updateSearch", data);
        }
      }
    }, 300),
    async searchProject() {
      let val = this.search;
      if (val) {
        this.$store.commit("updateProjectSearchAll", false);
        const {
          data: { code, data }
        } = await this.$api.search.company.searchForProject(this.filter, val);
        if (code === REQUEST.SUCCESS.code) {
          this.$emit("updateSearch", data);
        }
      } else {
        this.$notification.warn("请输入要搜索的项目");
      }
    },
    clear() {
      this.search = null;
      this.$emit("searchValue", null);
      this.$emit("cancel");
    },
    created() {
      this.clear();
    },
    beforeDestroy() {
      this.clear();
    }
  }
};
</script>

<style scoped></style>
