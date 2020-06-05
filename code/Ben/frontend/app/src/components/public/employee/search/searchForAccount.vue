<template>
  <div>
    <v-text-field
      v-model.trim="search"
      v-if="active"
      class="ma-3"
      label="搜索成员"
      style="max-width: 200px;"
      append-icon="mdi-magnify"
      clear-icon="mdi-close"
      outlined
      dense
      clearable
      hide-details
      v-on:click:clear="clear"
      v-on:click:append="searchAccount"
      @input="handleSearch"
    ></v-text-field>
  </div>
</template>

<script>
import debounce from "lodash/debounce";
import { REQUEST } from "../../../../common/view/Constant";

export default {
  name: "searchForAccount",
  props: {
    active: {
      type: Boolean,
      default: false
    },
    filter: {
      type: Object
    },
    type: {
      type: String
    }
  },
  data: () => ({ search: "" }),
  methods: {
    handleSearch: debounce(async function(val) {
      if (val) {
        this.$store.commit("updateEmployeeSearchAll", false);
        this.$emit("searchValue", val);
        let dataCode;
        let dataList;
        switch (this.type) {
          case "company": {
            const {
              data: { code, data }
            } = await this.$api.search.account.searchAccountByCompanyId(
              this.filter,
              val
            );
            dataCode = code;
            dataList = data;
            break;
          }
          case "team": {
            const {
              data: { code, data }
            } = await this.$api.search.account.searchAccountByTeamId(
              this.filter,
              val
            );
            dataCode = code;
            dataList = data;
            break;
          }
          case "project": {
            const {
              data: { code, data }
            } = await this.$api.search.account.searchAccountByProjectId(
              this.filter,
              val
            );
            dataCode = code;
            dataList = data;
            break;
          }
        }
        if (dataCode === REQUEST.SUCCESS.code) {
          this.$emit("updateSearch", dataList);
        }
      }
    }, 300),
    searchAccount() {
      let val = this.search;
      if (val) {
        this.handleSearch();
      } else {
        this.$notification.warn("请输入要搜索的账号");
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
