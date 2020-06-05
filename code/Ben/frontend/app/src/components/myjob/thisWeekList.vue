<template>
  <div style="min-height: 500px;position: relative">
    <v-subheader>
      本周排班
    </v-subheader>
    <v-list v-if="pageInfo.data.length > 0" three-line>
      <template v-for="(item, index) in pageInfo.data">
        <v-list-item
          :key="item.id"
          @click="$router.push(`/projects/${item.projectId}`)"
        >
          <v-list-item-avatar>
            <v-icon :color="item.color">mdi-checkbox-blank-circle</v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ item.name }}</v-list-item-title>
            <v-list-item-subtitle>
              {{ item.description ? item.description : "暂无描述" }}
            </v-list-item-subtitle>
            <v-list-item-subtitle class="mt-2" style="font-weight: bold">
              {{ item.start }} - {{ item.end }}
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
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn
                  v-on="on"
                  icon
                  large
                  @click="$router.push(`/projects/${item.projectId}`)"
                >
                  <v-icon color="grey lighten-1">mdi-information</v-icon>
                </v-btn>
              </template>
              <span>前往项目</span>
            </v-tooltip>
          </v-list-item-action>
        </v-list-item>
        <v-divider :key="index" inset></v-divider>
      </template>
    </v-list>
    <div v-if="pageInfo.data.length > 0">
      <v-pagination
        v-model="pageFilter.pageNumber"
        :length="pageInfo.pages"
        :total-visible="5"
      ></v-pagination>
    </div>
    <div
      v-else
      class="d-flex justify-center"
      style="position: relative;width: 100%"
    >
      <a-empty class="em-box" description="没有任务"></a-empty>
    </div>
  </div>
</template>

<script>
import { REQUEST } from "../../common/view/Constant";
import { mapMutations } from "vuex";
import { Empty } from "ant-design-vue";
export default {
  name: "thisWeekList",
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
      data: []
    }
  }),
  methods: {
    ...mapMutations({
      updateThisWeekCount: "updateMyJobThisWeekCount"
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
      } = await this.$api.job.getThisWeekJobList(this.pageFilter);
      if (REQUEST.SUCCESS.code === code) {
        this.pageInfo.pages = data.pages;
        this.pageInfo.data = data.list;
        this.updateThisWeekCount(data.total);
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
