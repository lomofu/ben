<template>
  <v-row>
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-card-title>{{ getTitle }}</v-card-title>
        <v-card-text>
          <v-card flat min-height="150px">
            {{ getContent }}
          </v-card>
        </v-card-text>
        <v-spacer />
        <v-card-text>
          <div class="d-flex justify-space-between">
            <div>
              发布者:
              <v-avatar size="25" class="ml-1 mr-2">
                <v-img :src="item.avatarUrl"></v-img>
              </v-avatar>
              {{ getCreateBy }}
            </div>
            <div>
              {{
                item.createTime
                  ? $moment(item.createTime).format("lll")
                  : "未知时间"
              }}
            </div>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn outlined color="primary" @click="handleClose" text>关闭</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { isNull } from "../../../utils/object";
import { eventBus } from "../../../bus";

export default {
  name: "showNotificationDetail",
  data: () => ({
    dialog: false,
    item: null
  }),
  methods: {
    handleClose() {
      this.dialog = false;
    }
  },
  computed: {
    getTitle() {
      return isNull(this.item) ? "暂无标题" : this.item.title;
    },
    getContent() {
      return isNull(this.item) ? "暂无内容" : this.item.content;
    },
    getCreateBy() {
      return isNull(this.item) ? "未知" : this.item.accountName;
    }
  },
  created() {
    eventBus.$on("showNotificationDetail", data => {
      this.dialog = true;
      this.item = data;
    });
  },
  beforeDestroy() {
    eventBus.$off("showNotificationDetail");
  }
};
</script>

<style scoped></style>
