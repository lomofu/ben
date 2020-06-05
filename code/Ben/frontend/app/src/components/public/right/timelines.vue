<template>
  <v-card flat>
    <v-card-text>
      <v-timeline v-if="items.length > 0">
        <v-timeline-item v-for="(item, i) in items" :key="i" small left>
          <template v-slot:icon>
            <v-avatar size="35">
              <v-img :src="item.avatarUrl" :lazy-src="item.avatarUrl" />
            </v-avatar>
          </template>
          <template v-slot:opposite>
            <span>发布者: {{ item.accountName }}</span
            ><br />
            <span>{{ $moment(item.createTime).format("lll") }}</span>
          </template>
          <v-card>
            <v-card-title :class="dark ? '' : changeColor()">
              {{ i + 1 }} {{ item.title }}
            </v-card-title>
            <v-card-text class="mt-3">
              <p>
                {{ item.content }}
              </p>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" outlined small @click="showDetail(item)">
                  详情
                </v-btn>
              </v-card-actions>
            </v-card-text>
          </v-card>
        </v-timeline-item>
      </v-timeline>
      <div v-else>
        <a-empty description="暂无通知" />
      </div>
      <div v-if="pageInfo.hasNextPage" class=" mt-3 d-flex justify-center">
        <v-btn color="primary" outlined @click="next" :loading="isDisable">
          加载更多
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
import { PAGE, REQUEST } from "../../../common/view/Constant";
import { Empty } from "ant-design-vue";
import { eventBus } from "../../../bus";

const color = [
  "red white--text",
  "pink white--text",
  "purple white--text",
  "indigo white--text",
  "cyan white--text",
  "teal white--text",
  "green white--text",
  "orange white--text",
  "brown white--text"
];
export default {
  name: "timelines",
  components: {
    aEmpty: Empty
  },
  data: () => ({
    items: [],
    isDisable: false,
    pageFilter: {
      pageNumber: PAGE.PAGE_MIN_VAL,
      pageSize: PAGE.PAGE_SIZE
    },
    pageInfo: {
      list: [],
      hasNextPage: false,
      total: 0
    }
  }),
  methods: {
    changeColor() {
      let index = Math.round(Math.random() * 8);
      return color[index > 8 ? Math.round(Math.random() * 8) : index];
    },
    async fetchMessage() {
      try {
        const {
          data: { code, data }
        } = await this.$api.push.getMessage(this.account.id, this.pageFilter);
        if (REQUEST.SUCCESS.code === code) {
          this.pageInfo = data;
          data.list.forEach(e => this.items.push(e));
          this.$emit("updateCount", this.pageInfo.total);
        }
      } finally {
        this.isDisable = false;
      }
    },
    next() {
      if (this.pageInfo.hasNextPage) {
        this.isDisable = true;
        this.pageFilter.pageNumber++;
        this.fetchMessage();
      }
    },
    showDetail(val) {
      eventBus.$emit("showNotificationDetail", val);
    }
  },
  computed: {
    dark() {
      return this.$store.getters.getDark;
    },
    account() {
      return this.$store.getters.getAccount;
    }
  },
  created() {
    this.fetchMessage();
    eventBus.$on("fetchMessage", () => {
      this.fetchMessage();
    });
  },
  beforeDestroy() {
    eventBus.$off("fetchMessage");
  }
};
</script>

<style scoped>
p {
  width: 350px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
