<template>
  <div style="min-height: 500px;position: relative">
    <v-row>
      <v-subheader class="ma-3">所有任务</v-subheader>
      <v-expansion-panels v-if="pageInfo.data.length > 0">
        <v-expansion-panel
          v-for="(item, index) in pageInfo.data"
          :key="index"
          class="pa-3"
        >
          <v-expansion-panel-header>
            <div>
              <v-icon :color="item.color">
                mdi-checkbox-blank-circle
              </v-icon>
              <span class="ml-5">
                {{ item.name }} {{ item.start }} - {{ item.end }}
              </span>
              <v-chip
                v-if="item.full"
                small
                class="ma-2"
                :color="item.color"
                label
                text-color="white"
              >
                全天
              </v-chip>
            </div>
          </v-expansion-panel-header>
          <v-expansion-panel-content class="ma-3">
            {{ item.description ? item.description : "暂无描述" }}
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
      <div
        v-else
        class="d-flex justify-center"
        style="position: relative;width: 100%"
      >
        <a-empty class="em-box" description="没有任务"></a-empty>
      </div>
    </v-row>
    <v-row v-if="pageInfo.hasNext" class="ma-5" justify="center">
      <v-btn outlined>加载更多</v-btn>
    </v-row>
  </div>
</template>

<script>
import { Empty } from "ant-design-vue";
import { mapMutations } from "vuex";
import { REQUEST } from "../../common/view/Constant";

export default {
  name: "totalJobList",
  components: {
    aEmpty: Empty
  },
  data: () => ({
    pageFilter: {
      pageNumber: 1,
      pageSize: 5,
      data: null
    },
    pageInfo: {
      pages: 0,
      data: [],
      hasNext: false
    }
  }),
  methods: {
    ...mapMutations({
      updateTotalCount: "updateMyJobTotalCount"
    }),
    addFilter() {
      let pageFilter = this.pageFilter;
      pageFilter.data = this.account.id;
      if (pageFilter.pageNumber < 1) {
        pageFilter.pageNumber = 1;
      }
    },
    async fetchData() {
      this.addFilter();
      const {
        data: { code, data }
      } = await this.$api.job.getAllJobList(this.pageFilter);
      if (REQUEST.SUCCESS.code === code) {
        this.pageInfo.pages = data.pages;
        data.list.forEach(e => this.pageInfo.data.push(e));
        this.pageInfo.hasNext = data.hasNextPage;
        this.updateTotalCount(data.total);
      }
    }
  },
  computed: {
    account() {
      return this.$store.getters.getAccount;
    }
  },
  created() {
    this.fetchData();
  }
};
</script>

<style scoped>
.em-box {
  position: absolute;
  top: 15vh;
}
</style>
